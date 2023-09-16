package com.sports.scraper.api.utils;

import org.jsoup.select.Elements;

import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.player.nba.NBAPlayerGameLogDto;
import com.sports.scraper.domain.teams.nba.NBATeamPerGame;

public class NBAPlayerMapperUtils {

    private NBAPlayerMapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static PlayerPerGameStatsDto mapPlayerPerGameStatsRow(Elements playerAttributes) {

        PlayerPerGameStatsDto playerDto = new PlayerPerGameStatsDto();
        playerDto.setPlayerName(playerAttributes.get(0).text());
        playerDto.setPlayerUrl(
                URLUtils.SCRAPING_NBA_URL + MapperUtils.getElementUrl(playerAttributes.get(0)));
        int lastForwardSlash = playerDto.getPlayerUrl().lastIndexOf("/") + 1;
        int lastPeriod = playerDto.getPlayerUrl().lastIndexOf(".");
        playerDto.setSystemName(playerDto.getPlayerUrl().substring(lastForwardSlash, lastPeriod));
        playerDto.setPosition(playerAttributes.get(1).text());
        playerDto.setAge(Integer.parseInt(playerAttributes.get(2).text()));
        playerDto.setTeam(playerAttributes.get(3).text());
        playerDto.setGamesCount(Integer.parseInt(playerAttributes.get(4).text()));
        playerDto.setGamesStartedCount(Integer.parseInt(playerAttributes.get(5).text()));
        playerDto.setMinutesPlayed(MapperUtils.getFloat(playerAttributes.get(6).text()));
        playerDto.setFieldGoals(MapperUtils.getFloat(playerAttributes.get(7).text()));
        playerDto.setFieldGoalAttempts(MapperUtils.getFloat(playerAttributes.get(8).text()));
        playerDto.setFieldGoalPercentage(MapperUtils.getDouble(playerAttributes.get(9).text()));
        playerDto.setThreePointers(MapperUtils.getFloat(playerAttributes.get(10).text()));
        playerDto.setThreePointerAttempts(MapperUtils.getFloat(playerAttributes.get(11).text()));
        playerDto.setThreePointerPercentage(MapperUtils.getDouble(playerAttributes.get(12).text()));
        playerDto.setTwoPointers(MapperUtils.getFloat(playerAttributes.get(13).text()));
        playerDto.setTwoPointerAttempts(MapperUtils.getFloat(playerAttributes.get(14).text()));
        playerDto.setTwoPointerPercentage(MapperUtils.getDouble(playerAttributes.get(15).text()));
        playerDto.setEffectiveFieldGoalPercentage(MapperUtils.getDouble(playerAttributes.get(16).text()));
        playerDto.setFreeThrows(MapperUtils.getFloat(playerAttributes.get(17).text()));
        playerDto.setFreeThrowAttempts(MapperUtils.getFloat(playerAttributes.get(18).text()));
        playerDto.setFreeThrowPercentage(MapperUtils.getDouble(playerAttributes.get(19).text()));
        playerDto.setOffensiveRebounds(MapperUtils.getFloat(playerAttributes.get(20).text()));
        playerDto.setDefensiveRebounds(MapperUtils.getFloat(playerAttributes.get(21).text()));
        playerDto.setTotalRebounds(MapperUtils.getFloat(playerAttributes.get(22).text()));
        playerDto.setAssists(MapperUtils.getFloat(playerAttributes.get(23).text()));
        playerDto.setSteals(MapperUtils.getFloat(playerAttributes.get(24).text()));
        playerDto.setBlocks(MapperUtils.getFloat(playerAttributes.get(25).text()));
        playerDto.setTurnovers(MapperUtils.getFloat(playerAttributes.get(26).text()));
        playerDto.setPersonalFouls(MapperUtils.getFloat(playerAttributes.get(27).text()));
        playerDto.setPoints(MapperUtils.getFloat(playerAttributes.get(28).text()));

        return playerDto;
    }

    public static NBAPlayerGameLogDto mapNBAPlayerGameLogRow(Elements columns) {
        NBAPlayerGameLogDto responseDto = new NBAPlayerGameLogDto();
        responseDto.setGameCount(MapperUtils.getInteger(columns.get(0).text()));
        responseDto.setDate(columns.get(1).text());
        responseDto.setAge(columns.get(2).text());
        responseDto.setTeam(columns.get(3).text());
        responseDto.setAway(getIsAway(columns.get(4).text()));
        responseDto.setOpponent(columns.get(5).text());
        responseDto.setResult(columns.get(6).text());
        if (responseDto.getGameCount() == 0) {
            responseDto.setReason(columns.get(7).text());
            return responseDto;
        }
        responseDto.setStartedGame(getIsStarting(MapperUtils.getInteger(columns.get(7).text())));
        responseDto.setMinutesPlayed(columns.get(8).text());
        responseDto.setFieldGoals(MapperUtils.getInteger(columns.get(9).text()));
        responseDto.setFieldGoalAttempts(MapperUtils.getInteger(columns.get(10).text()));
        responseDto.setFieldGoalPercentage(MapperUtils.getDouble(columns.get(11).text()));
        responseDto.setThreePointers(MapperUtils.getInteger(columns.get(12).text()));
        responseDto.setThreePointerAttempts(MapperUtils.getInteger(columns.get(13).text()));
        responseDto.setThreePointerPercentage(MapperUtils.getDouble(columns.get(14).text()));
        responseDto.setFreeThrows(MapperUtils.getInteger(columns.get(15).text()));
        responseDto.setFreeThrowAttempts(MapperUtils.getInteger(columns.get(16).text()));
        responseDto.setFreeThrowPercentage(MapperUtils.getDouble(columns.get(17).text()));
        responseDto.setOffensiveRebounds(MapperUtils.getInteger(columns.get(18).text()));
        responseDto.setDefensiveRebounds(MapperUtils.getInteger(columns.get(19).text()));
        responseDto.setTotalRebounds(MapperUtils.getInteger(columns.get(20).text()));
        responseDto.setAssists(MapperUtils.getInteger(columns.get(21).text()));
        responseDto.setSteals(MapperUtils.getInteger(columns.get(22).text()));
        responseDto.setBlocks(MapperUtils.getInteger(columns.get(23).text()));
        responseDto.setTurnovers(MapperUtils.getInteger(columns.get(24).text()));
        responseDto.setPersonalFouls(MapperUtils.getInteger(columns.get(25).text()));
        responseDto.setPoints(MapperUtils.getInteger(columns.get(26).text()));
        responseDto.setGameScore(MapperUtils.getDouble(columns.get(27).text()));
        responseDto.setPlusMinus(columns.get(28).text());
        return responseDto;
    }

    public static PlayerAdvancedGameLogDto mapPlayerAdvancedGameLogRow(Elements columns) {
        PlayerAdvancedGameLogDto responseDto = new PlayerAdvancedGameLogDto();
        responseDto.setGameCount(MapperUtils.getInteger(columns.get(0).text()));
        responseDto.setDate(columns.get(1).text());
        responseDto.setAge(columns.get(2).text());
        responseDto.setTeam(columns.get(3).text());
        responseDto.setAway(getIsAway(columns.get(4).text()));
        responseDto.setOpponent(columns.get(5).text());
        responseDto.setResult(columns.get(6).text());
        if (responseDto.getGameCount() == 0) {
            responseDto.setReason(columns.get(7).text());
            return responseDto;
        }
        responseDto.setStartedGame(getIsStarting(MapperUtils.getInteger(columns.get(7).text())));
        responseDto.setMinutesPlayed(columns.get(8).text());
        responseDto.setTrueShootingPercent(columns.get(9).text());
        responseDto.setEffectiveFieldGoalPercent(columns.get(10).text());
        responseDto.setOffensiveReboundPercent(columns.get(11).text());
        responseDto.setDefensiveReboundPercent(columns.get(12).text());
        responseDto.setTotalReboundPercent(columns.get(13).text());
        responseDto.setAssistPercent(columns.get(14).text());
        responseDto.setStealPercent(columns.get(15).text());
        responseDto.setBlockPercent(columns.get(16).text());
        responseDto.setTurnoverPercent(columns.get(17).text());
        responseDto.setUsagePercent(columns.get(18).text());
        responseDto.setOffensiveRating(MapperUtils.getInteger(columns.get(19).text()));
        responseDto.setDefenseRating(MapperUtils.getInteger(columns.get(20).text()));
        responseDto.setGameScore(columns.get(21).text());
        responseDto.setBoxPlusMinus(columns.get(22).text());
        return responseDto;
    }

    public static NBATeamPerGame mapTeamPerGameStatsRow(Elements columns) {
        NBATeamPerGame responseDto = new NBATeamPerGame();
        responseDto.setTeamName(columns.get(0).text());

        Elements urlElements = columns.get(0).getElementsByTag("a");
        if (!urlElements.isEmpty()) {
            responseDto.setTeamUrl(URLUtils.SCRAPING_NBA_URL + urlElements.first().attr("href"));

        }

        responseDto.setGamesPlayed(MapperUtils.getInteger(columns.get(1).text()));
        responseDto.setMinutesPlayed(MapperUtils.getDouble(columns.get(2).text()));
        responseDto.setFieldGoals(MapperUtils.getDouble(columns.get(3).text()));
        responseDto.setFieldGoalAttempts(MapperUtils.getDouble(columns.get(4).text()));
        responseDto.setFieldGoalPercentage(MapperUtils.getDouble(columns.get(5).text()));
        responseDto.setThreePointers(MapperUtils.getDouble(columns.get(6).text()));
        responseDto.setThreePointerAttempts(MapperUtils.getDouble(columns.get(7).text()));
        responseDto.setThreePointerPercentage(MapperUtils.getDouble(columns.get(8).text()));
        responseDto.setTwoPointers(MapperUtils.getDouble(columns.get(9).text()));
        responseDto.setTwoPointerAttempts(MapperUtils.getDouble(columns.get(10).text()));
        responseDto.setTwoPointerPercentage(MapperUtils.getDouble(columns.get(11).text()));
        responseDto.setFreeThrows(MapperUtils.getDouble(columns.get(12).text()));
        responseDto.setFreeThrowAttempts(MapperUtils.getDouble(columns.get(13).text()));
        responseDto.setFreeThrowPercentage(MapperUtils.getDouble(columns.get(14).text()));
        responseDto.setOffensiveRebounds(MapperUtils.getDouble(columns.get(15).text()));
        responseDto.setDefensiveRebounds(MapperUtils.getDouble(columns.get(16).text()));
        responseDto.setTotalRebounds(MapperUtils.getDouble(columns.get(17).text()));
        responseDto.setAssists(MapperUtils.getDouble(columns.get(18).text()));
        responseDto.setSteals(MapperUtils.getDouble(columns.get(19).text()));
        responseDto.setBlocks(MapperUtils.getDouble(columns.get(20).text()));
        responseDto.setTurnovers(MapperUtils.getDouble(columns.get(21).text()));
        responseDto.setPersonalFouls(MapperUtils.getDouble(columns.get(22).text()));
        responseDto.setPoints(MapperUtils.getDouble(columns.get(23).text()));
        return responseDto;
    }

    private static boolean getIsAway(String text) {
        return text.length() > 0;
    }

    private static boolean getIsStarting(int isStartingInt) {
        return isStartingInt == 1;
    }
}
