package com.sports.scraper.api.utils;

import org.jsoup.select.Elements;

import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.player.nba.NBAPlayerGameLogDto;
import com.sports.scraper.domain.player.nfl.WideReceiverGameLogDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

public class MapperUtils {

    private MapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static PlayerPerGameStatsDto mapPlayerPerGameStatsRow(Elements playerAttributes) {

        PlayerPerGameStatsDto playerDto = new PlayerPerGameStatsDto();
        playerDto.setPlayerName(playerAttributes.get(0).text());
        playerDto.setPlayerUrl(
                URLUtils.SCRAPING_NBA_URL + playerAttributes.get(0).select("a[href]").first().attr("href"));
        int lastForwardSlash = playerDto.getPlayerUrl().lastIndexOf("/") + 1;
        int lastPeriod = playerDto.getPlayerUrl().lastIndexOf(".");
        playerDto.setSystemName(playerDto.getPlayerUrl().substring(lastForwardSlash, lastPeriod));
        playerDto.setPosition(playerAttributes.get(1).text());
        playerDto.setAge(Integer.parseInt(playerAttributes.get(2).text()));
        playerDto.setTeam(playerAttributes.get(3).text());
        playerDto.setGamesCount(Integer.parseInt(playerAttributes.get(4).text()));
        playerDto.setGamesStartedCount(Integer.parseInt(playerAttributes.get(5).text()));
        playerDto.setMinutesPlayed(getFloat(playerAttributes.get(6).text()));
        playerDto.setFieldGoals(getFloat(playerAttributes.get(7).text()));
        playerDto.setFieldGoalAttempts(getFloat(playerAttributes.get(8).text()));
        playerDto.setFieldGoalPercentage(getDouble(playerAttributes.get(9).text()));
        playerDto.setThreePointers(getFloat(playerAttributes.get(10).text()));
        playerDto.setThreePointerAttempts(getFloat(playerAttributes.get(11).text()));
        playerDto.setThreePointerPercentage(getDouble(playerAttributes.get(12).text()));
        playerDto.setTwoPointers(getFloat(playerAttributes.get(13).text()));
        playerDto.setTwoPointerAttempts(getFloat(playerAttributes.get(14).text()));
        playerDto.setTwoPointerPercentage(getDouble(playerAttributes.get(15).text()));
        playerDto.setEffectiveFieldGoalPercentage(getDouble(playerAttributes.get(16).text()));
        playerDto.setFreeThrows(getFloat(playerAttributes.get(17).text()));
        playerDto.setFreeThrowAttempts(getFloat(playerAttributes.get(18).text()));
        playerDto.setFreeThrowPercentage(getDouble(playerAttributes.get(19).text()));
        playerDto.setOffensiveRebounds(getFloat(playerAttributes.get(20).text()));
        playerDto.setDefensiveRebounds(getFloat(playerAttributes.get(21).text()));
        playerDto.setTotalRebounds(getFloat(playerAttributes.get(22).text()));
        playerDto.setAssists(getFloat(playerAttributes.get(23).text()));
        playerDto.setSteals(getFloat(playerAttributes.get(24).text()));
        playerDto.setBlocks(getFloat(playerAttributes.get(25).text()));
        playerDto.setTurnovers(getFloat(playerAttributes.get(26).text()));
        playerDto.setPersonalFouls(getFloat(playerAttributes.get(27).text()));
        playerDto.setPoints(getFloat(playerAttributes.get(28).text()));

        return playerDto;
    }

    public static NBAPlayerGameLogDto mapNBAPlayerGameLogRow(Elements columns) {
        NBAPlayerGameLogDto responseDto = new NBAPlayerGameLogDto();
        responseDto.setGameCount(getInteger(columns.get(0).text()));
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
        responseDto.setStartedGame(getIsStarting(getInteger(columns.get(7).text())));
        responseDto.setMinutesPlayed(columns.get(8).text());
        responseDto.setFieldGoals(getInteger(columns.get(9).text()));
        responseDto.setFieldGoalAttempts(getInteger(columns.get(10).text()));
        responseDto.setFieldGoalPercentage(getDouble(columns.get(11).text()));
        responseDto.setThreePointers(getInteger(columns.get(12).text()));
        responseDto.setThreePointerAttempts(getInteger(columns.get(13).text()));
        responseDto.setThreePointerPercentage(getDouble(columns.get(14).text()));
        responseDto.setFreeThrows(getInteger(columns.get(15).text()));
        responseDto.setFreeThrowAttempts(getInteger(columns.get(16).text()));
        responseDto.setFreeThrowPercentage(getDouble(columns.get(17).text()));
        responseDto.setOffensiveRebounds(getInteger(columns.get(18).text()));
        responseDto.setDefensiveRebounds(getInteger(columns.get(19).text()));
        responseDto.setTotalRebounds(getInteger(columns.get(20).text()));
        responseDto.setAssists(getInteger(columns.get(21).text()));
        responseDto.setSteals(getInteger(columns.get(22).text()));
        responseDto.setBlocks(getInteger(columns.get(23).text()));
        responseDto.setTurnovers(getInteger(columns.get(24).text()));
        responseDto.setPersonalFouls(getInteger(columns.get(25).text()));
        responseDto.setPoints(getInteger(columns.get(26).text()));
        responseDto.setGameScore(getDouble(columns.get(27).text()));
        responseDto.setPlusMinus(columns.get(28).text());
        return responseDto;
    }

    public static WideReceiverGameLogDto mapNFLPlayerGameLogRow(Elements columns) {
        WideReceiverGameLogDto responseDto = new WideReceiverGameLogDto();
        responseDto.setDate(columns.get(0).text());
        responseDto.setGameCount(getInteger(columns.get(1).text()));
        responseDto.setWeek(getInteger(columns.get(2).text()));
        responseDto.setAge(columns.get(3).text());
        responseDto.setTeam(columns.get(4).text());
        responseDto.setAway(getIsAway(columns.get(5).text()));
        responseDto.setOpponent(columns.get(6).text());
        responseDto.setResult(columns.get(7).text());
        if (responseDto.getAge().isBlank()) {
            responseDto.setReason(columns.get(8).text());
            return responseDto;
        }
        responseDto.setStartedGame(columns.get(8).text().equalsIgnoreCase("*"));
        return responseDto;
    }

    public static PlayerAdvancedGameLogDto mapPlayerAdvancedGameLogRow(Elements columns) {
        PlayerAdvancedGameLogDto responseDto = new PlayerAdvancedGameLogDto();
        responseDto.setGameCount(getInteger(columns.get(0).text()));
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
        responseDto.setStartedGame(getIsStarting(getInteger(columns.get(7).text())));
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
        responseDto.setOffensiveRating(getInteger(columns.get(19).text()));
        responseDto.setDefenseRating(getInteger(columns.get(20).text()));
        responseDto.setGameScore(columns.get(21).text());
        responseDto.setBoxPlusMinus(columns.get(22).text());
        return responseDto;
    }

    public static TeamPerGameDto mapTeamPerGameStatsRow(Elements columns) {
        TeamPerGameDto responseDto = new TeamPerGameDto();
        responseDto.setTeamName(columns.get(0).text());

        Elements urlElements = columns.get(0).getElementsByTag("a");
        if (!urlElements.isEmpty()) {
            responseDto.setTeamUrl(URLUtils.SCRAPING_NBA_URL + urlElements.first().attr("href"));

        }

        responseDto.setGamesPlayed(getInteger(columns.get(1).text()));
        responseDto.setMinutesPlayed(getDouble(columns.get(2).text()));
        responseDto.setFieldGoals(getDouble(columns.get(3).text()));
        responseDto.setFieldGoalAttempts(getDouble(columns.get(4).text()));
        responseDto.setFieldGoalPercentage(getDouble(columns.get(5).text()));
        responseDto.setThreePointers(getDouble(columns.get(6).text()));
        responseDto.setThreePointerAttempts(getDouble(columns.get(7).text()));
        responseDto.setThreePointerPercentage(getDouble(columns.get(8).text()));
        responseDto.setTwoPointers(getDouble(columns.get(9).text()));
        responseDto.setTwoPointerAttempts(getDouble(columns.get(10).text()));
        responseDto.setTwoPointerPercentage(getDouble(columns.get(11).text()));
        responseDto.setFreeThrows(getDouble(columns.get(12).text()));
        responseDto.setFreeThrowAttempts(getDouble(columns.get(13).text()));
        responseDto.setFreeThrowPercentage(getDouble(columns.get(14).text()));
        responseDto.setOffensiveRebounds(getDouble(columns.get(15).text()));
        responseDto.setDefensiveRebounds(getDouble(columns.get(16).text()));
        responseDto.setTotalRebounds(getDouble(columns.get(17).text()));
        responseDto.setAssists(getDouble(columns.get(18).text()));
        responseDto.setSteals(getDouble(columns.get(19).text()));
        responseDto.setBlocks(getDouble(columns.get(20).text()));
        responseDto.setTurnovers(getDouble(columns.get(21).text()));
        responseDto.setPersonalFouls(getDouble(columns.get(22).text()));
        responseDto.setPoints(getDouble(columns.get(23).text()));
        return responseDto;
    }

    private static boolean getIsAway(String text) {
        return text.length() > 0;
    }

    private static boolean getIsStarting(int isStartingInt) {
        return isStartingInt == 1;
    }

    private static float getFloat(String text) {
        if (text != null && text.length() > 0) {
            return Float.parseFloat(text);
        }
        return 0.0f;
    }

    private static double getDouble(String text) {
        if (text != null && text.length() > 0) {
            return Double.parseDouble(text);
        }
        return 0.0;
    }

    private static int getInteger(String text) {
        if (text != null && text.length() > 0) {
            return Integer.parseInt(text);
        }
        return 0;
    }
}
