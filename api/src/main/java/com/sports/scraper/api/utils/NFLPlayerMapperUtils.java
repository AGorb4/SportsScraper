package com.sports.scraper.api.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;

import com.sports.scraper.domain.player.nfl.gamelog.NFLPlayerGameLogDto;
import com.sports.scraper.domain.player.nfl.stats.FumbleStatsDto;
import com.sports.scraper.domain.player.nfl.stats.KickReturnStatsDto;
import com.sports.scraper.domain.player.nfl.stats.PassingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.ReceivingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.RushingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.ScoringStatsDto;
import com.sports.scraper.domain.player.nfl.stats.SnapStatsDto;
import com.sports.scraper.domain.player.nfl.stats.TacklingStatsDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsFactory;

public class NFLPlayerMapperUtils {

    private static Map<String, Integer> columnCountMap;

    private NFLPlayerMapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static NFLPlayerGameLogDto mapNFLPlayerGameLogDto(Elements columns,
            List<String> statCategoriesList) {
        NFLPlayerGameLogDto nflPlayerGameLogDto = new NFLPlayerGameLogDto();
        nflPlayerGameLogDto.setDate(columns.get(0).text());
        nflPlayerGameLogDto.setGameCount(MapperUtils.getInteger(columns.get(1).text()));
        nflPlayerGameLogDto.setWeek(MapperUtils.getInteger(columns.get(2).text()));
        nflPlayerGameLogDto.setAge(columns.get(3).text());
        nflPlayerGameLogDto.setTeam(columns.get(4).text());
        nflPlayerGameLogDto.setAway(columns.get(5).text().contains("@"));
        nflPlayerGameLogDto.setOpponent(columns.get(6).text());
        nflPlayerGameLogDto.setResult(columns.get(7).text());
        nflPlayerGameLogDto.setStartedGame(columns.get(8).text().contains("*"));
        int i = 8;
        if (columns.get(8).text().length() < 2) {
            for (String s : statCategoriesList) {
                if (s.equalsIgnoreCase("Receiving")) {
                    mapReceivingStatsGameLog(i, columns, nflPlayerGameLogDto);
                    i = i + 7;
                } else if (s.equalsIgnoreCase("Rushing")) {
                    mapRushingStatsGameLog(i, columns, nflPlayerGameLogDto);
                    i = i + 4;
                } else if (s.equalsIgnoreCase("Passing")) {
                    mapPassingStatsGameLog(i, columns, nflPlayerGameLogDto);
                    i = i + 11;
                } else if (s.contains("Returns")) {
                    KickReturnStatsDto kickReturnStatsDto = new KickReturnStatsDto();
                    kickReturnStatsDto.setReturns(MapperUtils.getInteger(columns.get(++i).text()));
                    kickReturnStatsDto.setYards(MapperUtils.getInteger(columns.get(++i).text()));
                    kickReturnStatsDto.setYardsPerReturn(columns.get(++i).text());
                    kickReturnStatsDto.setReturnTouchdowns(MapperUtils.getInteger(columns.get(++i).text()));

                    if (s.contains("Kick")) {
                        nflPlayerGameLogDto.setKickReturnStats(kickReturnStatsDto);
                    } else if (s.contains("Punt")) {
                        nflPlayerGameLogDto.setPuntReturnStats(kickReturnStatsDto);
                    }
                } else if (s.equalsIgnoreCase("Scoring")) {
                    mapScoringStatsGameLog(i, columns, nflPlayerGameLogDto);
                    i = i + 2;
                } else if (s.equalsIgnoreCase("Tackles")) {
                    mapTacklesStatsGameLog(i, columns, nflPlayerGameLogDto);
                    i = i + 6;
                } else if (s.equalsIgnoreCase("Fumbles")) {
                    mapFumbleStatsGameLog(i, columns, nflPlayerGameLogDto);
                    i = i + 6;
                } else if (s.equalsIgnoreCase("Snaps")) {
                    SnapStatsDto snapStatsDto = new SnapStatsDto();
                    snapStatsDto.setSnapsNum(MapperUtils.getInteger(columns.get(++i).text()));
                    snapStatsDto.setSnapsPct(columns.get(++i).text());
                    if (s.contains("Def")) {
                        nflPlayerGameLogDto.setDefSnapStats(snapStatsDto);
                    } else if (s.contains("Off")) {
                        nflPlayerGameLogDto.setOffSnapStats(snapStatsDto);
                    } else if (s.contains("ST")) {
                        nflPlayerGameLogDto.setStSnapStats(snapStatsDto);
                    }
                    i = i + 2;
                }
            }
        } else {
            nflPlayerGameLogDto.setReason(columns.get(8).text());
        }

        return nflPlayerGameLogDto;
    }

    public static NFLPlayerFantasyStatsDto mapNFLPlayerFantasyStatsRow(Elements columns, int playerRanking) {
        NFLPlayerFantasyStatsDto nflPlayerFantasyStatsDto = NFLPlayerFantasyStatsFactory
                .createNFLPlayer(columns.get(2).text());

        nflPlayerFantasyStatsDto.setRank(playerRanking);
        nflPlayerFantasyStatsDto
                .setPlayerName(columns.get(0).text().replace("*", StringUtils.EMPTY).replace("+", StringUtils.EMPTY));
        nflPlayerFantasyStatsDto.setPlayerUrl(
                URLUtils.SCRAPING_NFL_URL + MapperUtils.getElementUrl(columns.get(0)));
        int lastForwardSlash = nflPlayerFantasyStatsDto.getPlayerUrl().lastIndexOf("/") + 1;
        int lastPeriod = nflPlayerFantasyStatsDto.getPlayerUrl().lastIndexOf(".");
        nflPlayerFantasyStatsDto
                .setPlayerSystemName(nflPlayerFantasyStatsDto.getPlayerUrl().substring(lastForwardSlash, lastPeriod));
        nflPlayerFantasyStatsDto.setTeam(columns.get(1).text());
        nflPlayerFantasyStatsDto.setPosition(columns.get(2).text());

        // games played
        nflPlayerFantasyStatsDto.setAge(MapperUtils.getInteger(columns.get(3).text()));
        nflPlayerFantasyStatsDto.setGamesPlayed(MapperUtils.getInteger(columns.get(4).text()));
        nflPlayerFantasyStatsDto.setGamesStarted(MapperUtils.getInteger(columns.get(5).text()));
        // passing stats
        nflPlayerFantasyStatsDto.setPassesCompleted(MapperUtils.getInteger(columns.get(6).text()));
        nflPlayerFantasyStatsDto.setPassesAttempted(MapperUtils.getInteger(columns.get(7).text()));
        nflPlayerFantasyStatsDto.setPassingYards(MapperUtils.getInteger(columns.get(8).text()));
        nflPlayerFantasyStatsDto.setPassingTouchdowns(MapperUtils.getInteger(columns.get(9).text()));
        nflPlayerFantasyStatsDto.setInterceptions(MapperUtils.getInteger(columns.get(10).text()));
        // rushing stats
        nflPlayerFantasyStatsDto.setRushingAttempts(MapperUtils.getInteger(columns.get(11).text()));
        nflPlayerFantasyStatsDto.setRushingYards(MapperUtils.getInteger(columns.get(12).text()));
        nflPlayerFantasyStatsDto.setRushingYardsPerAttempt(columns.get(13).text());
        nflPlayerFantasyStatsDto.setRushingTouchdowns(MapperUtils.getInteger(columns.get(14).text()));
        // receiving stats
        nflPlayerFantasyStatsDto.setTargets(MapperUtils.getInteger(columns.get(15).text()));
        nflPlayerFantasyStatsDto.setReceptions(MapperUtils.getInteger(columns.get(16).text()));
        nflPlayerFantasyStatsDto.setReceivingYards(MapperUtils.getInteger(columns.get(17).text()));
        nflPlayerFantasyStatsDto.setReceivingYardsPerReception(columns.get(18).text());
        nflPlayerFantasyStatsDto.setReceivingTouchdowns(MapperUtils.getInteger(columns.get(19).text()));

        // fumbles
        nflPlayerFantasyStatsDto.setFumbles(MapperUtils.getInteger(columns.get(20).text()));
        nflPlayerFantasyStatsDto.setFumblesLost(MapperUtils.getInteger(columns.get(21).text()));

        // scoring
        nflPlayerFantasyStatsDto.setTouchdowns(MapperUtils.getInteger(columns.get(22).text()));
        nflPlayerFantasyStatsDto.setTwoPointConversionMade(MapperUtils.getInteger(columns.get(23).text()));
        nflPlayerFantasyStatsDto.setTwoPointConversionPasses(MapperUtils.getInteger(columns.get(24).text()));
        // fantasy
        nflPlayerFantasyStatsDto.setFantasyPoints(MapperUtils.getInteger(columns.get(25).text()));
        nflPlayerFantasyStatsDto.setFantasyPointsPpr(columns.get(26).text());
        nflPlayerFantasyStatsDto.setFantasyVbd(MapperUtils.getInteger(columns.get(29).text()));
        nflPlayerFantasyStatsDto.setFantasyPositionRank(MapperUtils.getInteger(columns.get(30).text()));
        nflPlayerFantasyStatsDto.setFantasyOverallRank(MapperUtils.getInteger(columns.get(31).text()));
        return nflPlayerFantasyStatsDto;
    }

    public static void mapStatCategory(int i, String category, Elements columns) {
        Integer columnsInCategory = columnCountMap.get(category);

    }

    private static void mapReceivingStatsGameLog(int i, Elements columns, NFLPlayerGameLogDto gameLogDto) {
        ReceivingStatsDto receivingStats = new ReceivingStatsDto();
        receivingStats.setTargets(MapperUtils.getInteger(columns.get(++i).text()));
        receivingStats.setReceptions(MapperUtils.getInteger(columns.get(++i).text()));
        receivingStats.setReceivingYards(MapperUtils.getInteger(columns.get(++i).text()));
        receivingStats.setYardsPerReception(columns.get(++i).text());
        receivingStats.setReceivingTouchdowns(MapperUtils.getInteger(columns.get(++i).text()));
        receivingStats.setCatchPercentage(columns.get(++i).text());
        receivingStats.setYardsPerTarget(columns.get(++i).text());
        gameLogDto.setReceivingStats(receivingStats);
    }

    private static void mapRushingStatsGameLog(int i, Elements columns, NFLPlayerGameLogDto gameLogDto) {
        RushingStatsDto rushingStats = new RushingStatsDto();
        rushingStats.setRushingAttempts(MapperUtils.getInteger(columns.get(++i).text()));
        rushingStats.setRushingYards(MapperUtils.getInteger(columns.get(++i).text()));
        rushingStats.setRushingYardsPerCarry(columns.get(++i).text());
        rushingStats.setRushingTouchdowns(MapperUtils.getInteger(columns.get(++i).text()));
        gameLogDto.setRushingStats(rushingStats);
    }

    private static void mapPassingStatsGameLog(int i, Elements columns, NFLPlayerGameLogDto gameLogDto) {
        PassingStatsDto passingStatsDto = new PassingStatsDto();
        passingStatsDto.setPassesCompleted(MapperUtils.getInteger(columns.get(++i).text()));
        passingStatsDto.setPassesAttempted(MapperUtils.getInteger(columns.get(++i).text()));
        passingStatsDto.setCompletionPercentage(columns.get(++i).text());
        passingStatsDto.setPassingYards(MapperUtils.getInteger(columns.get(++i).text()));
        passingStatsDto.setPassingTouchdowns(MapperUtils.getInteger(columns.get(++i).text()));
        passingStatsDto.setInterceptions(MapperUtils.getInteger(columns.get(++i).text()));
        passingStatsDto.setPasserRating(columns.get(++i).text());
        passingStatsDto.setSacks(columns.get(++i).text());
        passingStatsDto.setSackYards(MapperUtils.getInteger(columns.get(++i).text()));
        passingStatsDto.setPassingYardsPerAttempt(columns.get(++i).text());
        passingStatsDto.setAirYardsPerAttempt(columns.get(++i).text());
        gameLogDto.setPassingStats(passingStatsDto);
    }

    private static void mapScoringStatsGameLog(int i, Elements columns, NFLPlayerGameLogDto gameLogDto) {
        ScoringStatsDto scoringStatsDto = new ScoringStatsDto();
        scoringStatsDto.setTouchdowns(MapperUtils.getInteger(columns.get(++i).text()));
        scoringStatsDto.setPointsScored(MapperUtils.getInteger(columns.get(++i).text()));
        gameLogDto.setScoringStats(scoringStatsDto);
    }

    private static void mapTacklesStatsGameLog(int i, Elements columns, NFLPlayerGameLogDto gameLogDto) {
        TacklingStatsDto tacklingStats = new TacklingStatsDto();
        tacklingStats.setSacks(columns.get(++i).text());
        tacklingStats.setSoloTackles(columns.get(++i).text());
        tacklingStats.setAssistedTackles(MapperUtils.getInteger(columns.get(++i).text()));
        tacklingStats.setTacklesCombo(MapperUtils.getInteger(columns.get(++i).text()));
        if (!StringUtils.isBlank(columns.get(i + 1).text())) {
            tacklingStats.setTacklesForLoss(MapperUtils.getInteger(columns.get(++i).text()));
        }
        if (!StringUtils.isBlank(columns.get(i + 1).text())) {
            tacklingStats.setQbHits(MapperUtils.getInteger(columns.get(++i).text()));
        }
        gameLogDto.setTacklingStats(tacklingStats);
    }

    private static void mapFumbleStatsGameLog(int i, Elements columns, NFLPlayerGameLogDto gameLogDto) {
        FumbleStatsDto fumbleStats = new FumbleStatsDto();
        fumbleStats.setFumbles(MapperUtils.getInteger(columns.get(++i).text()));
        fumbleStats.setFumblesLost(MapperUtils.getInteger(columns.get(++i).text()));
        fumbleStats.setFumblesForced(MapperUtils.getInteger(columns.get(++i).text()));
        fumbleStats.setFumblesRecovered(MapperUtils.getInteger(columns.get(++i).text()));
        fumbleStats.setFumbleYards(columns.get(++i).text());
        fumbleStats.setFumblesTouchdowns(MapperUtils.getInteger(columns.get(++i).text()));
        gameLogDto.setFumbleStats(fumbleStats);
    }

    public static String getNFLPlayerFantasyPosition(Elements playerAttributes) {
        String playerPosition = playerAttributes.get(2).text();
        if (!StringUtils.isBlank(playerPosition) && !playerPosition.equalsIgnoreCase("FantPos")) {
            return playerPosition;
        }
        return null;
    }

    @PostConstruct
    private static Map<String, Integer> constructColumnCountMap() {
        columnCountMap = new HashMap<>();
        columnCountMap.put("Receiving", 7);
        columnCountMap.put("Rushing", 4);
        columnCountMap.put("Passing", 11);
        columnCountMap.put("Kick Returns", 4);
        columnCountMap.put("Scoring", 2);
        columnCountMap.put("Tackles", 6);
        columnCountMap.put("Fumbles", 6);
        columnCountMap.put("Snaps", 2);
        return columnCountMap;
    }
}
