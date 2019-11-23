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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DatasService {

    private static final Logger log = LoggerFactory.getLogger(DatasService.class);

    private static String PATTERN_DATE = "uuuu/MM/dd HH:mm:ss";
    private static String EURO = "\u20ac";

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
    private PlayerHandRepository playerHandRepository;

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
            parseHistory = new ParseHistory()
                .fileSize(((Long) channel.size()).intValue())
                .fileName(fileName)
                .parsedDate(Instant.now());
            this.parseHistoryRepository.save(parseHistory);
        }

        // Me
        Optional<Player> myPlayerOptional = this.playerRepository.findByName(this.myName);
        Player myPlayer = new Player();
        if (!myPlayerOptional.isPresent()) {
            myPlayer.addedDate(Instant.now())
                .name(this.myName);
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
        boolean raisedPreFlop = false;
        Hand lastHand = null;
        String line;
        while ((line = file.readLine()) != null) {
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
                raisedPreFlop = false;
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
                raisedPreFlop = false;

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
                raisedPreFlop = this.treatAction(BettingRound.PRE_FLOP, line, game, hand, playersInHand, raisedPreFlop);
            } else if (isFlop) {
                this.treatAction(BettingRound.FLOP, line, game, hand, playersInHand, false);
            } else if (isTurn) {
                this.treatAction(BettingRound.TURN, line, game, hand, playersInHand, false);
            } else if (isRiver) {
                this.treatAction(BettingRound.RIVER, line, game, hand, playersInHand, false);
            } else if (isShowDown) {
                this.treatAction(BettingRound.SHOW_DOWN, line, game, hand, playersInHand, false);
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
                this.showDownRepository.save(new ShowDown()
                    .wins(true)
                    .hand(hand)
                    .player(player));
            }
        }
    }

    private void treatShow(String line, Hand hand, Map<String, Player> playersInHand) {
        Player player = playersInHand.get(StringUtils.substringBefore(line, " shows "));

        this.showDownRepository.save(new ShowDown()
            .hand(hand)
            .cards("[" + StringUtils.substringBetween(line, " shows [ ", "]") + "]")
            .player(player));
    }

    private boolean treatAction(BettingRound bettingRound, String line, Game game, Hand hand, Map<String, Player> playersInHand,
                                boolean raisedPreFlop) {
        PlayerAction playerAction = new PlayerAction()
            .game(game)
            .hand(hand)
            .bettingRound(bettingRound);

        Player player = null;
        if (line.contains(" folds")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " folds"));
            playerAction.player(player)
                .action(Action.FOLDS);
        } else if (line.contains(" calls ") && line.contains(" and is all-in")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " calls "));
            playerAction.player(player)
                .action(Action.CALLS)
                .amount(this.convertToAmount(StringUtils.substringBetween(line, " calls ",
                    " and is all-in")));
        } else if (line.contains(" calls ")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " calls "));
            playerAction.player(player)
                .action(Action.CALLS)
                .amount(this.convertToAmount(StringUtils.substringAfter(line, " calls ")));
        } else if (line.contains(" raises ") && line.contains(" and is all-in")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " raises "));
            playerAction.player(player)
                .action(Action.RAISES)
                .amount(this.convertToAmount(
                    StringUtils.substringBetween(StringUtils.substringAfter(line, " raises "), " to ", " and is all-in")));
        } else if (line.contains(" raises ")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " raises "));
            playerAction.player(player)
                .action(Action.RAISES)
                .amount(this.convertToAmount(
                    StringUtils.substringAfter(StringUtils.substringAfter(line, " raises "), " to ")));
        } else if (line.contains(" checks")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " checks"));
            playerAction.player(player)
                .action(Action.CHECKS);
        } else if (line.contains(" bets ") && line.contains(" and is all-in")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " bets "));
            playerAction.player(player)
                .action(Action.BETS)
                .amount(this.convertToAmount(StringUtils.substringBetween(line, " bets ",
                    " and is all-in")));
        } else if (line.contains(" bets ")) {
            player = playersInHand.get(StringUtils.substringBefore(line, " bets "));
            playerAction.player(player)
                .action(Action.BETS)
                .amount(this.convertToAmount(StringUtils.substringAfter(line, " bets ")));
        }

        if (player != null) {
            if (bettingRound == BettingRound.PRE_FLOP && !raisedPreFlop && playerAction.getAction() == Action.RAISES) {
                raisedPreFlop = true;

                // Raises PréFlop
                Optional<PlayerHand> playerHandOptional = this.playerHandRepository.findByHandAndPlayer(hand, player);
                if (playerHandOptional.isPresent()) {
                    this.playerHandRepository.save(playerHandOptional.get().raisesPf(true));
                } else {
                    this.playerHandRepository.save(new PlayerHand()
                        .player(player)
                        .hand(hand)
                        .raisesPf(true));
                }
            } else if (bettingRound == BettingRound.PRE_FLOP && playerAction.getAction() == Action.RAISES) {
                // 3bet
                Optional<PlayerHand> playerHandOptional = this.playerHandRepository.findByHandAndPlayer(hand, player);
                if (playerHandOptional.isPresent()) {
                    this.playerHandRepository.save(playerHandOptional.get().threeBetPf(true).raisesPf(true));
                } else {
                    this.playerHandRepository.save(new PlayerHand()
                        .player(player)
                        .hand(hand)
                        .threeBetPf(true)
                        .raisesPf(true));
                }
            } else if (bettingRound == BettingRound.PRE_FLOP && playerAction.getAction() == Action.CALLS) {
                // Calls PréFlop
                Optional<PlayerHand> playerHandOptional = this.playerHandRepository.findByHandAndPlayer(hand, player);
                if (playerHandOptional.isPresent()) {
                    this.playerHandRepository.save(playerHandOptional.get().callsPf(true));
                } else {
                    this.playerHandRepository.save(new PlayerHand()
                        .player(player)
                        .hand(hand)
                        .callsPf(true));
                }
            } else if (bettingRound == BettingRound.FLOP && playerAction.getAction() == Action.BETS) {
                // Bets Flop
                Optional<PlayerHand> playerHandOptional = this.playerHandRepository.findByHandAndPlayer(hand, player);
                if (playerHandOptional.isPresent()) {
                    this.playerHandRepository.save(playerHandOptional.get().betsFlop(true));
                } else {
                    this.playerHandRepository.save(new PlayerHand()
                        .player(player)
                        .hand(hand)
                        .betsFlop(true));
                }
            } else if (bettingRound == BettingRound.FLOP && playerAction.getAction() == Action.CALLS) {
                // Bets Flop
                Optional<PlayerHand> playerHandOptional = this.playerHandRepository.findByHandAndPlayer(hand, player);
                if (playerHandOptional.isPresent()) {
                    this.playerHandRepository.save(playerHandOptional.get().callsFlop(true));
                } else {
                    this.playerHandRepository.save(new PlayerHand()
                        .player(player)
                        .hand(hand)
                        .callsFlop(true));
                }
            } else if (bettingRound == BettingRound.FLOP && playerAction.getAction() == Action.RAISES) {
                // Bets Flop
                Optional<PlayerHand> playerHandOptional = this.playerHandRepository.findByHandAndPlayer(hand, player);
                if (playerHandOptional.isPresent()) {
                    this.playerHandRepository.save(playerHandOptional.get().raisesFlop(true));
                } else {
                    this.playerHandRepository.save(new PlayerHand()
                        .player(player)
                        .hand(hand)
                        .raisesFlop(true));
                }
            }
        }

        this.playerActionRepository.save(playerAction);

        return raisedPreFlop;
    }

    private void treatActionAnteBlinds(String line, Game game, Hand hand, Map<String, Player> playersInHand) {
        if (line.contains("Dealt to ")) {
            hand.setMyCards("[" + StringUtils.substringBetween(line, "[", "]") + "]");
            return;
        }

        PlayerAction playerAction = new PlayerAction()
            .game(game)
            .hand(hand)
            .bettingRound(BettingRound.ANTE_BLINDS);

        if (line.contains(" posts big blind ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts big blind"));
            playerAction.player(player)
                .action(Action.POSTS_BB)
                .amount(this.convertToAmount(StringUtils.substringBetween(line, " posts big blind ",
                    " and is all-in")));
        } else if (line.contains(" posts big blind ") && line.contains(" out of position")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts big blind"));
            playerAction.player(player)
                .action(Action.POSTS_BB)
                .amount(this.convertToAmount(StringUtils.substringBetween(line, " posts big blind ",
                    " out of position")));
        } else if (line.contains(" posts big blind ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts big blind"));
            playerAction.player(player)
                .action(Action.POSTS_BB)
                .amount(this.convertToAmount(StringUtils.substringAfter(line, " posts big blind ")));
        } else if (line.contains(" posts small blind ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts small blind"));
            playerAction.player(player)
                .action(Action.POSTS_SB)
                .amount(this.convertToAmount(StringUtils.substringBetween(line, " posts small blind ",
                    " and is all-in")));
        } else if (line.contains(" posts small blind ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts small blind"));
            playerAction.player(player)
                .action(Action.POSTS_SB)
                .amount(this.convertToAmount(StringUtils.substringAfter(line, " posts small blind ")));
        } else if (line.contains(" posts ante ") && line.contains(" and is all-in")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts ante"));
            playerAction.player(player)
                .action(Action.POSTS_ANTE)
                .amount(this.convertToAmount(StringUtils.substringBetween(line, " posts ante ",
                    " and is all-in")));
        } else if (line.contains(" posts ante ")) {
            Player player = playersInHand.get(StringUtils.substringBefore(line, " posts ante"));
            playerAction.player(player)
                .action(Action.POSTS_ANTE)
                .amount(this.convertToAmount(StringUtils.substringAfter(line, " posts ante ")));
        }

        this.playerActionRepository.save(playerAction);
    }

    private Player findPlayer(String line) {
        String playerName = StringUtils.substringsBetween(line, ": ", " (")[0];
        Optional<Player> optionalPlayer = this.playerRepository.findByName(playerName);

        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get();
        } else {
            Player player = new Player()
                .addedDate(Instant.now())
                .name(playerName);

            return this.playerRepository.save(player);
        }
    }

    private Game createGame(String line, Player me) {
        return new Game()
            .player(me)
            .name(line)
            .startDate(LocalDateTime.parse(
                StringUtils.substringsBetween(line, ") - ", " UTC")[0],
                DateTimeFormatter.ofPattern(DatasService.PATTERN_DATE, Locale.getDefault()))
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    private Hand createHand(String line, Game game) {
        return new Hand()
            .game(game)
            .startDate(LocalDateTime.parse(
                StringUtils.substringsBetween(line, ") - ", " UTC")[0],
                DateTimeFormatter.ofPattern(DatasService.PATTERN_DATE, Locale.getDefault()))
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    private Double convertToAmount(String amount) {
        return Double.valueOf(amount.replaceAll("[^0-9.]", ""));
    }
}
