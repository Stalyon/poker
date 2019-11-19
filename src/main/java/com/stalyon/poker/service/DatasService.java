package com.stalyon.poker.service;

import com.stalyon.poker.domain.*;
import com.stalyon.poker.domain.enumeration.Action;
import com.stalyon.poker.domain.enumeration.BettingRound;
import com.stalyon.poker.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
public class DatasService {

    private static final Logger log = LoggerFactory.getLogger(DatasService.class);

    private static String PATTERN_DATE = "uuuu/MM/dd HH:mm:ss";

    @Value("${poker.histo-path}")
    private String histoPath;

    @Value("${poker.me}")
    private String myName;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private HandRepository handRepository;

    @Autowired
    private LiveService liveService;

    @Autowired
    private ParseHistoryRepository parseHistoryRepository;

    @Autowired
    private PlayerActionRepository playerActionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShowDownRepository showDownRepository;

    public void update() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(this.histoPath))) {
            paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toFile().getName().endsWith(".txt"))
                .filter(path -> !path.toFile().getName().endsWith("_summary.txt"))
                .forEach(path -> {
                    try {
                        log.info("File {} is processing", path.toFile().getName());
                        this.treatFile(path.toFile().getName(), false);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                });
        }
    }

    public void treatFile(String fileName, boolean notify) throws IOException {
        RandomAccessFile file = new RandomAccessFile(this.histoPath + fileName, "r");
        FileChannel channel = file.getChannel();
        boolean hasChanged = false;
        ParseHistory parseHistory;

        // Vérification que le file n'a pas déjà été traité
        Optional<ParseHistory> parseHistoryOptional = this.parseHistoryRepository.findByFileName(fileName);
        if (parseHistoryOptional.isPresent()) {
            parseHistory = parseHistoryOptional.get();

            if (parseHistory.getFileSize() == ((Long) channel.size()).intValue()) {
                log.info("File is already processed.");

                channel.close();
                file.close();
                return;
            } else {
                log.info("File already processed but has changed.");

                parseHistory.setFileSize(((Long) channel.size()).intValue());
                this.parseHistoryRepository.save(parseHistory);
                hasChanged = true;
            }
        } else {
            parseHistory = new ParseHistory();
            parseHistory.setFileSize(((Long) channel.size()).intValue());
            parseHistory.setFileName(fileName);
            parseHistory.setParsedDate(Instant.now());
            this.parseHistoryRepository.save(parseHistory);
        }

        // Me
        Optional<Player> myPlayerOptional = this.playerRepository.findByName(this.myName);
        Player myPlayer = new Player();
        if (!myPlayerOptional.isPresent()) {
            myPlayer.setAddedDate(Instant.now());
            myPlayer.setName(this.myName);
            myPlayer.setIsMe(true);
        } else {
            myPlayer = myPlayerOptional.get();
        }
        myPlayer = this.playerRepository.save(myPlayer);

        Game game = null;
        Hand hand = null;
        Map<String, Player> playersInHand = new HashMap<>();
        boolean isAnteBlinds = false;
        boolean isPreFlop = false;
        boolean isFlop = false;
        boolean isTurn = false;
        boolean isRiver = false;
        boolean isShowDown = false;
        boolean isSummary = false;
        boolean waitNextHand = false;
        Hand lastHand = null;
        String line;
        while((line = file.readLine()) != null) {
            if (hasChanged) {
                if (waitNextHand && line.contains("Winamax Poker - ")) {
                    hasChanged = false;
                    isSummary = false;

                    if (game == null) {
                        // Game
                        game = this.gameRepository.save(this.createGame(line, myPlayer));
                        parseHistory.setGame(game);
                        parseHistory = this.parseHistoryRepository.save(parseHistory);
                    }

                    // Hand
                    hand = this.handRepository.save(this.createHand(line, game));
                } else if (line.contains("Winamax Poker - ")) {
                    Instant lastHandInstant = parseHistory.getGame().getEndDate();
                    String lastHandDate = DateTimeFormatter.ofPattern(DatasService.PATTERN_DATE, Locale.getDefault())
                        .withZone(ZoneId.systemDefault())
                        .format(lastHandInstant);

                    if (line.contains(lastHandDate)) {
                        game = parseHistory.getGame();
                        waitNextHand = true;
                    }
                }
            } else if (line.contains("Winamax Poker - ")) {
                isSummary = false;

                if (game == null) {
                    // Game
                    game = this.gameRepository.save(this.createGame(line, myPlayer));
                    parseHistory.setGame(game);
                    parseHistory = this.parseHistoryRepository.save(parseHistory);
                }

                // Hand
                hand = this.handRepository.save(this.createHand(line, game));
            } else if (!isSummary && line.startsWith("Seat ")) {
                // Players
                Player player = this.findPlayer(line);
                playersInHand.put(player.getName(), player);
            } else if (line.startsWith("*** ANTE/BLINDS ***")) {
                isAnteBlinds = true;
            } else if (line.startsWith("*** PRE-FLOP *** ")) {
                isAnteBlinds = false;
                isPreFlop = true;
            } else if (line.startsWith("*** FLOP ***")) {
                isPreFlop = false;
                isFlop = true;
                hand.setFlopCards(this.getCards(line));

                hand = this.handRepository.save(hand);
            } else if (line.startsWith("*** TURN ***")) {
                isFlop = false;
                isTurn = true;
                hand.setTurnCards(this.getCards(line));

                hand = this.handRepository.save(hand);
            } else if (line.startsWith("*** RIVER ***")) {
                isTurn = false;
                isRiver = true;
                hand.setRiverCards(this.getCards(line));

                hand = this.handRepository.save(hand);
            } else if (line.startsWith("*** SHOW DOWN ***")) {
                isRiver = false;
                isShowDown = true;
            } else if (line.startsWith("*** SUMMARY ***")) {
                isAnteBlinds = false;
                isPreFlop = false;
                isFlop = false;
                isTurn = false;
                isRiver = false;
                isShowDown = false;
                isSummary = true;

                // Fin de la main
                game.setEndDate(hand.getStartDate());
                lastHand = this.handRepository.save(hand);
                game = this.gameRepository.save(game);

                // Remise à 0
                hand = null;
                playersInHand = new HashMap<>();
            } else if (line.contains(" shows ")) {
                this.treatShow(line, hand, playersInHand);
            } else if (line.contains(" collected ")) {
                this.treatCollect(line, hand, playersInHand);
            } else if (isAnteBlinds) {
                this.treatActionAnteBlinds(line, game, hand, playersInHand);
            } else if (isPreFlop) {
                this.treatAction(BettingRound.PRE_FLOP, line, game, hand, playersInHand);
            } else if (isFlop) {
                this.treatAction(BettingRound.FLOP, line, game, hand, playersInHand);
            } else if (isTurn) {
                this.treatAction(BettingRound.TURN, line, game, hand, playersInHand);
            } else if (isRiver) {
                this.treatAction(BettingRound.RIVER, line, game, hand, playersInHand);
            } else if (isShowDown) {
                this.treatAction(BettingRound.SHOW_DOWN, line, game, hand, playersInHand);
            }
        }

        if (notify && lastHand != null) {
            this.liveService.sendNotif(game, lastHand);
        }

        channel.close();
        file.close();
    }

    private String getCards(String line) {
        return "[" + StringUtils.substringAfter(line, " *** [");
    }

    private void treatCollect(String line, Hand hand, Map<String, Player> playersInHand) {
        Player player = playersInHand.get(StringUtils.substringBefore(line, " collected "));

        if (line.contains(" from pot") || line.contains(" from main pot")) {
            Optional<ShowDown> showDownOptional = this.showDownRepository.findByHandAndPlayer(hand, player);
            hand.setWinner(player);

            if (showDownOptional.isPresent()) {
                showDownOptional.get().setWins(true);
                this.showDownRepository.save(showDownOptional.get());
            } else {
                ShowDown showDown = new ShowDown();
                showDown.setWins(true);
                showDown.setHand(hand);
                showDown.setPlayer(player);
            }
        }
    }

    private void treatShow(String line, Hand hand, Map<String, Player> playersInHand) {
        ShowDown showDown = new ShowDown();
        showDown.setHand(hand);
        showDown.setCards("[" + StringUtils.substringBetween(line, " shows [ ", "]") + "]");

        Player player = playersInHand.get(StringUtils.substringBefore(line, " shows "));
        showDown.setPlayer(player);

        this.showDownRepository.save(showDown);
    }

    private void treatAction(BettingRound bettingRound, String line, Game game, Hand hand, Map<String, Player> playersInHand) {
        PlayerAction playerAction = new PlayerAction();
        playerAction.setGame(game);
        playerAction.setHand(hand);
        playerAction.setBettingRound(bettingRound);

        if (line.contains(" folds")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " folds"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.FOLDS);
        } else if (line.contains(" calls ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " calls "));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.CALLS);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringBetween(line, " calls ",
                " and is all-in")));
        } else if (line.contains(" calls ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " calls "));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.CALLS);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringAfter(line, " calls ")));
        } else if (line.contains(" raises ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " raises "));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.RAISES);
            playerAction.setAmount(this.convertToAmount(
                StringUtils.substringBetween(StringUtils.substringAfter(line, " raises "), " to "," and is all-in")));
        } else if (line.contains(" raises ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " raises "));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.RAISES);
            playerAction.setAmount(this.convertToAmount(
                StringUtils.substringAfter(StringUtils.substringAfter(line, " raises "), " to ")));
        } else if (line.contains(" checks")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " checks"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.CHECKS);
        } else if (line.contains(" bets ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " bets "));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.BETS);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringBetween(line, " bets ",
                " and is all-in")));
        } else if (line.contains(" bets ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " bets "));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.BETS);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringAfter(line, " bets ")));
        }

        this.playerActionRepository.save(playerAction);
    }

    private void treatActionAnteBlinds(String line, Game game, Hand hand, Map<String, Player> playersInHand) {
        if (line.contains("Dealt to ")) {
            hand.setMyCards("[" + StringUtils.substringBetween(line, "[", "]") + "]");
            return;
        }

        PlayerAction playerAction = new PlayerAction();
        playerAction.setGame(game);
        playerAction.setHand(hand);
        playerAction.setBettingRound(BettingRound.ANTE_BLINDS);

        if (line.contains(" posts big blind ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts big blind"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.POSTS_BB);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringBetween(line, " posts big blind ",
                " and is all-in")));
        } else if (line.contains(" posts big blind ") && line.contains(" out of position")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts big blind"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.POSTS_BB);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringBetween(line, " posts big blind ",
                " out of position")));
        } else if (line.contains(" posts big blind ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts big blind"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.POSTS_BB);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringAfter(line, " posts big blind ")));
        } else if (line.contains(" posts small blind ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts small blind"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.POSTS_SB);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringBetween(line, " posts small blind ",
                " and is all-in")));
        } else if (line.contains(" posts small blind ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts small blind"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.POSTS_SB);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringAfter(line, " posts small blind ")));
        } else if (line.contains(" posts ante ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts ante"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.POSTS_ANTE);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringBetween(line, " posts ante ",
                " and is all-in")));
        } else if (line.contains(" posts ante ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts ante"));
            playerAction.setPlayer(player);
            playerAction.setAction(Action.POSTS_ANTE);
            playerAction.setAmount(this.convertToAmount(StringUtils.substringAfter(line, " posts ante ")));
        }

        this.playerActionRepository.save(playerAction);
    }

    private Player findPlayer(String line) {
        String playerName = StringUtils.substringsBetween(line, ": ", " (")[0];
        Optional<Player> optionalPlayer = this.playerRepository.findByName(playerName);

        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get();
        } else {
            Player player = new Player();
            player.setAddedDate(Instant.now());
            player.setName(playerName);

            return this.playerRepository.save(player);
        }
    }

    private Game createGame(String line, Player me) {
        Game game = new Game();
        game.setPlayer(me);
        game.setName(line);

        game.setStartDate(LocalDateTime.parse(
            StringUtils.substringsBetween(line, ") - ", " UTC")[0],
            DateTimeFormatter.ofPattern(DatasService.PATTERN_DATE, Locale.getDefault()))
            .atZone(ZoneId.systemDefault()).toInstant());

        return game;
    }

    private Hand createHand(String line, Game game) {
        Hand hand = new Hand();
        hand.setGame(game);
        hand.setStartDate(LocalDateTime.parse(
            StringUtils.substringsBetween(line, ") - ", " UTC")[0],
            DateTimeFormatter.ofPattern(DatasService.PATTERN_DATE, Locale.getDefault()))
            .atZone(ZoneId.systemDefault()).toInstant());

        return hand;
    }

    private Double convertToAmount(String amount) {
        return Double.valueOf(amount.replaceAll("[^0-9.]", ""));
    }
}
