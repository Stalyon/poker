package com.stalyon.poker.service;

import com.google.common.util.concurrent.AtomicDouble;
import com.stalyon.poker.domain.CashGame;
import com.stalyon.poker.domain.GameHistory;
import com.stalyon.poker.domain.SitAndGo;
import com.stalyon.poker.domain.Tournoi;
import com.stalyon.poker.domain.enumeration.GameType;
import com.stalyon.poker.domain.enumeration.SitAndGoFormat;
import com.stalyon.poker.repository.CashGameRepository;
import com.stalyon.poker.repository.GameHistoryRepository;
import com.stalyon.poker.repository.SitAndGoRepository;
import com.stalyon.poker.repository.TournoiRepository;
import com.stalyon.poker.web.dto.StatsDto;
import com.stalyon.poker.web.dto.charts.ChartOptionDto;
import com.stalyon.poker.web.dto.charts.SerieDto;
import com.stalyon.poker.web.dto.charts.XAxisDto;
import com.stalyon.poker.web.dto.charts.YAxisDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatsService {

    private static final Logger log = LoggerFactory.getLogger(StatsService.class);

    private static String PATTERN_DATE = "dd/MM/uuuu HH:mm:ss";

    @Value("${poker.stats-path}")
    private String statsPath;

    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    @Autowired
    private CashGameRepository cashGameRepository;

    @Autowired
    private TournoiRepository tournoiRepository;

    @Autowired
    private SitAndGoRepository sitAndGoRepository;

    public void update() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(this.statsPath))) {
            paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toFile().getName().endsWith(".csv"))
                .forEach(this::treatFile);
        }
    }

    public void treatFile(Path path) {
        try {
            log.info("File {} is processing", path.toFile().getName());

            if (path.toFile().getName().contains("cashgame")) {
                this.treatCashGameFile(path.toFile().getName());
            } else if (path.toFile().getName().contains("tournois")) {
                this.treatTournoisFile(path.toFile().getName());
            } else if (path.toFile().getName().contains("sitandgo")) {
                this.treatSitAndGoFile(path.toFile().getName());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public StatsDto getStats() {
        List<GameHistory> gameHistorys = this.gameHistoryRepository.findAllByOrderByStartDate();

        DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.FRANCE)
                .withZone(ZoneId.systemDefault());
        DecimalFormat decimalFormatter = new DecimalFormat("#.##");

        XAxisDto xAxis = new XAxisDto(
            "category",
            gameHistorys
                .stream()
                .map(GameHistory::getStartDate)
                .map(dateFormatter::format)
                .collect(Collectors.toList())
        );
        YAxisDto yAxis = new YAxisDto("value");

        List<SerieDto> series = new ArrayList<>();
        SerieDto serie = new SerieDto(
            "line",
            "€",
            this.toCumulativeSumStream(gameHistorys
                .stream()
                .map(GameHistory::getNetResult))
                .map(netResult -> Double.valueOf(decimalFormatter.format(netResult).replace(",", ".")))
                .collect(Collectors.toList())
        );
        series.add(serie);
        ChartOptionDto chartOption = new ChartOptionDto(xAxis, yAxis, series);

        return new StatsDto(chartOption);
    }

    private Stream<Double> toCumulativeSumStream(Stream<Double> doubles){
        AtomicDouble sum = new AtomicDouble(0);
        return doubles.sequential().map(sum::addAndGet);
    }

    private void treatCashGameFile(String fileName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(this.statsPath + fileName, "r");

        String line;
        while((line = file.readLine()) != null) {
            String[] rows = StringUtils.split(line, ";");
            if (rows == null || rows.length != 6) {
                log.error("File is not correct");
                continue;
            }

            Instant startDate = LocalDateTime.parse(rows[0],
                    DateTimeFormatter.ofPattern(StatsService.PATTERN_DATE, Locale.getDefault()))
                .atZone(ZoneId.systemDefault()).toInstant();

            Optional<GameHistory> gameHistoryOptional = this.gameHistoryRepository.findByStartDateAndName(startDate, rows[2]);
            if (gameHistoryOptional.isPresent()) {
                continue;
            }

            Instant endDate = LocalDateTime.parse(rows[1],
                DateTimeFormatter.ofPattern(StatsService.PATTERN_DATE, Locale.getDefault()))
                .atZone(ZoneId.systemDefault()).toInstant();

            Double profit = Double.parseDouble(StringUtils.replace(rows[4], ",", "."));

            GameHistory gameHistory = new GameHistory();
            gameHistory.setStartDate(startDate);
            gameHistory.setName(rows[2]);
            gameHistory.setType(GameType.CASH_GAME);
            gameHistory.setNetResult(profit);

            CashGame cashGame = new CashGame();
            cashGame.setEndDate(endDate);
            cashGame.setNbHands(Integer.parseInt(rows[5]));
            cashGame.setSbBb(rows[3]);
            cashGame.setProfit(profit);
            cashGame.setTable(rows[2]);
            this.cashGameRepository.save(cashGame);

            gameHistory.setCashGame(cashGame);
            this.gameHistoryRepository.save(gameHistory);
        }
    }

    private void treatTournoisFile(String fileName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(this.statsPath + fileName, "r");

        String line;
        while((line = file.readLine()) != null) {
            String[] rows = StringUtils.split(line, ";");
            if (rows == null || rows.length != 8) {
                log.error("File is not correct");
                continue;
            }

            Instant startDate = LocalDateTime.parse(rows[0],
                DateTimeFormatter.ofPattern(StatsService.PATTERN_DATE, Locale.getDefault()))
                .atZone(ZoneId.systemDefault()).toInstant();

            Optional<GameHistory> gameHistoryOptional = this.gameHistoryRepository.findByStartDateAndName(startDate, rows[1]);
            if (gameHistoryOptional.isPresent()) {
                continue;
            }

            Double buyIn = Double.parseDouble(StringUtils.replace(rows[2], ",", "."));
            Double bounty = this.parseEuroAmountToDouble(rows[7]);
            Double rebuy = this.parseEuroAmountToDouble(rows[4]);
            Double profit = this.parseEuroAmountToDouble(rows[6]);

            GameHistory gameHistory = new GameHistory();
            gameHistory.setStartDate(startDate);
            gameHistory.setName(rows[1]);
            gameHistory.setType(GameType.TOURNOI);
            gameHistory.setNetResult(bounty - rebuy - buyIn + profit);

            Tournoi tournoi = new Tournoi();
            tournoi.setBounty(bounty);
            tournoi.setBuyIn(buyIn);
            tournoi.setRebuy(rebuy);
            tournoi.setRanking(Integer.parseInt(rows[5]));
            tournoi.setProfit(profit);
            this.tournoiRepository.save(tournoi);

            gameHistory.setTournoi(tournoi);
            this.gameHistoryRepository.save(gameHistory);
        }
    }

    private void treatSitAndGoFile(String fileName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(this.statsPath + fileName, "r");

        String line;
        while((line = file.readLine()) != null) {
            String[] rows = StringUtils.split(line, ";");
            if (rows == null || rows.length != 9) {
                log.error("File is not correct");
                continue;
            }

            Instant startDate = LocalDateTime.parse(rows[0],
                DateTimeFormatter.ofPattern(StatsService.PATTERN_DATE, Locale.getDefault()))
                .atZone(ZoneId.systemDefault()).toInstant();

            Optional<GameHistory> gameHistoryOptional = this.gameHistoryRepository.findByStartDateAndName(startDate, rows[1]);
            if (gameHistoryOptional.isPresent()) {
                continue;
            }

            Double buyIn = Double.parseDouble(StringUtils.replace(rows[2], ",", "."));
            Double bounty = this.parseEuroAmountToDouble(rows[7]);
            Double profit = this.parseEuroAmountToDouble(rows[6]);

            GameHistory gameHistory = new GameHistory();
            gameHistory.setStartDate(startDate);
            gameHistory.setName(rows[1]);
            gameHistory.setType(GameType.SIT_AND_GO);
            gameHistory.setNetResult(bounty - buyIn + profit);

            SitAndGo sitAndGo = new SitAndGo();
            sitAndGo.setBounty(bounty);
            sitAndGo.setRanking(Integer.parseInt(rows[5]));
            sitAndGo.setProfit(profit);
            sitAndGo.setBuyIn(buyIn);
            sitAndGo.setFormat(this.getSitAndGoFormat(rows[4]));
            this.sitAndGoRepository.save(sitAndGo);

            gameHistory.setSitAndGo(sitAndGo);
            this.gameHistoryRepository.save(gameHistory);
        }
    }

    private Double parseEuroAmountToDouble(String amount) {
        if (amount.equals("-") || amount.equals(" ")) {
            return 0.;
        }
        if (amount.contains("Ticket ")) {
            amount = StringUtils.substringAfter(amount, "Ticket ");
        }
        if (amount.matches("^[^0-9](.*)")) {
            return 0.;
        }

        if (amount.contains("Â")) {
            amount = StringUtils.substringBefore(amount, "Â");
        } else if (amount.contains("â")) {
            amount = StringUtils.substringBefore(amount, "â");
        }

        return Double.parseDouble(StringUtils.replace(amount, ",", "."));
    }

    private SitAndGoFormat getSitAndGoFormat(String row) {
        if (row.equals("SNG")) {
            return SitAndGoFormat.SNG;
        } else {
            return SitAndGoFormat.EXPRESSO;
        }
    }
}
