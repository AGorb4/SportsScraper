package com.sports.scraper.api.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ScraperServiceImpl implements ScraperService {

    @Override
    public Set<PlayerPerGameStatsDto> getPlayerPerGameForSeasonByTeam(int year, int pageSize) {
        Set<PlayerPerGameStatsDto> responseDtos = new HashSet<>();
        getPlayerPerGameForSeason(responseDtos, year, pageSize);
        return responseDtos;
    }

    private void getPlayerPerGameForSeason(Set<PlayerPerGameStatsDto> responseDtos, int year, int pageSize) {
        try {

            String url = "http://www.basketball-reference.com/leagues/NBA_" + year + "_per_game.html";

            Document document = Jsoup.connect(url).get();
            Elements playersList = document.getElementsByTag("tr");

            for (int i = 1; i < (pageSize > 0 ? pageSize : playersList.size()); i++) {
                if (!StringUtils.isEmpty(playersList.get(i).text())) {
                    Elements playerAttributes = playersList.get(i).getElementsByTag("td");
                    if (playerAttributes.size() > 0) {
                        responseDtos.add(mapPlayerPerGameStatsRow(playerAttributes));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private PlayerPerGameStatsDto mapPlayerPerGameStatsRow(Elements playerAttributes) {

        PlayerPerGameStatsDto playerDto = new PlayerPerGameStatsDto();
        playerDto.setPlayerName(playerAttributes.get(0).text());
        playerDto.setPlayerUrl(
                "http://www.basketball-reference.com" + playerAttributes.get(0).select("a[href]").first().attr("href"));
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

    private float getFloat(String text) {
        if (text != null && text.length() > 0) {
            return Float.parseFloat(text);
        }
        return 0.0f;
    }

    private double getDouble(String text) {
        if (text != null && text.length() > 0) {
            return Double.parseDouble(text);
        }
        return 0.0;
    }

    private int getInteger(String text) {
        if (text != null && text.length() > 0) {
            return Integer.parseInt(text);
        }
        return 0;
    }

    @Override
    public Set<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year) {
        Set<PlayerGameLogDto> responseDtos = new HashSet<>();
        getGameLog(responseDtos, player, year);
        return responseDtos;
    }

    private void getGameLog(Set<PlayerGameLogDto> responseDtos, String player, int year) {
        try {

            String url = "http://www.basketball-reference.com//players/" + player.charAt(0) + "/" + player + "/gamelog/"
                    + year;

            Document document = Jsoup.connect(url).get();
            Elements tables = document.getElementsByTag("tbody");
            Elements gamesList = tables.get(tables.size() - 1).getElementsByTag("tr");

            for (int i = 1; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag("td");
                    if (columns.size() > 0) {
                        responseDtos.add(mapPlayerGameLogRow(columns));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private PlayerGameLogDto mapPlayerGameLogRow(Elements columns) {
        PlayerGameLogDto responseDto = new PlayerGameLogDto();
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

    private boolean getIsAway(String text) {
        if (text.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean getIsStarting(int isStartingInt) {
        if (isStartingInt == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Set<TeamPerGameDto> getTeamPerGameStats(int year) {
        Set<TeamPerGameDto> responseDtos = new HashSet<>();
        getTeamPerGameStats(year, responseDtos);
        return responseDtos;
    }

    private void getTeamPerGameStats(int year, Set<TeamPerGameDto> responseDtos) {
        try {

            String url = "http://www.basketball-reference.com/leagues/NBA_" + year + ".html";

            Document document = Jsoup.connect(url).get();
            Element table = document.getElementById("per_game-team");
            Elements teamsList = table.getElementsByTag("tr");

            for (int i = 1; i < teamsList.size(); i++) {
                if (!StringUtils.isEmpty(teamsList.get(i).text())) {
                    Elements columns = teamsList.get(i).getElementsByTag("td");
                    if (columns.size() > 0) {
                        responseDtos.add(mapTeamPerGameStatsRow(columns));
                    }
                }
            }

            Element tableFooter = table.getElementsByTag("tfoot").get(0);
            Element footerRow = tableFooter.getElementsByTag("tr").get(0);
            responseDtos.add(mapTeamPerGameStatsRow(footerRow.getElementsByTag("td")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private TeamPerGameDto mapTeamPerGameStatsRow(Elements columns) {
        TeamPerGameDto responseDto = new TeamPerGameDto();
        responseDto.setTeamName(columns.get(0).text());

        Elements urlElements = columns.get(0).getElementsByTag("a");
        if (urlElements.size() > 0) {
            responseDto.setTeamUrl("http://www.basketball-reference.com" + urlElements.first().attr("href"));

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
}
