package com.sports.scraper.api.service.scraper.nfl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sports.scraper.api.constants.ScrapingConstants;
import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.api.service.scraper.ScraperService;
import com.sports.scraper.api.utils.NFLPlayerMapperUtils;
import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerNameDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.player.nfl.NFLPlayerProfileDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsDto;
import com.sports.scraper.domain.player.nfl.gamelog.NFLPlayerGameLogDto;
import com.sports.scraper.domain.player.nfl.stats.PassingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.ReceivingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.RushingStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

@Service("nflScraperServiceImpl")
public class NFLScraperServiceImpl implements ScraperService {

    public List<PlayerNameDto> getAllPlayerNames(int year) {
        List<PlayerNameDto> response = new ArrayList<>();
        List<NFLPlayerFantasyStatsDto> allPlayers = getAllNFLPlayersFantasyStats(year);
        for (NFLPlayerFantasyStatsDto fantasyStatsDto : allPlayers) {
            if (!fantasyStatsDto.getPosition().isBlank()) {
                PlayerNameDto playerName = new PlayerNameDto();
                playerName.setName(fantasyStatsDto.getPlayerName());
                playerName.setSystemName(fantasyStatsDto.getPlayerSystemName());
                playerName.setPosition(fantasyStatsDto.getPosition());
                playerName.setTeam(fantasyStatsDto.getTeam());
                response.add(playerName);
            }
        }
        if (response.size() > 0) {
            response.sort(Comparator.comparing(PlayerNameDto::getName));
        }
        return response;
    }

    public NFLPlayerProfileDto getNflPlayerProfile(String playerName, int year) {
        NFLPlayerProfileDto response = new NFLPlayerProfileDto();

        // set the general info
        NFLPlayerFantasyStatsDto player = getNFLPlayerFantasyStats(playerName, year);
        response.setPlayerName(player.getPlayerName());
        response.setAge(player.getAge());
        response.setGamesCount(player.getGamesPlayed());
        response.setGamesStartedCount(player.getGamesStarted());
        response.setPlayerUrl(player.getPlayerUrl());
        response.setPosition(player.getPosition());
        response.setSystemName(player.getPlayerSystemName());
        response.setTeam(player.getTeam());
        response.setPlayerPictureUrl(getPlayerPictureUrl(response.getSystemName()));

        // set the gamelog
        List<PlayerGameLogDto> playerGameLog = getPlayerGameLogForYear(playerName, year, true);
        response.setGameLog(playerGameLog);

        setPlayerPositionAverageStats(response);

        return response;
    }

    @Override
    public List<PlayerPerGameStatsDto> getPlayersPerGameForSeason(int year, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PlayerPerGameStatsDto getPlayerPerGameForSeason(String playerName, int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Cacheable(value = "nflPlayerGameLogForYear")
    public List<PlayerGameLogDto> getPlayerGameLogForYear(String playerName, int year, boolean sortCron) {
        List<PlayerGameLogDto> playerGameLogs = new ArrayList<>();
        List<NFLPlayerFantasyStatsDto> nflPlayers = getAllNFLPlayersFantasyStats(year);

        List<NFLPlayerFantasyStatsDto> filteredPlayers = nflPlayers.stream()
                .filter(player -> player.getPlayerName().equalsIgnoreCase(playerName))
                .collect(Collectors.toList());

        NFLPlayerFantasyStatsDto player = filteredPlayers.get(0);

        String url = "https://www.pro-football-reference.com/players/" + player.getPlayerSystemName().charAt(0) + "/"
                + player.getPlayerSystemName()
                + "/gamelog/"
                + year + "/";

        try {
            Document document = Jsoup.connect(url).get();
            Elements tables = document.getElementsByTag(ScrapingConstants.TABLE_BODY_TAG);

            Elements tableHeaders = document.getElementsByTag("table").get(0).getElementsByTag("thead").get(0)
                    .getElementsByTag("tr").get(0).getElementsByTag("th");

            Elements gamesList = tables.get(0).getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

            List<String> statsList = new ArrayList<>();

            for (Element header : tableHeaders) {
                if (!header.text().isBlank()) {
                    statsList.add(header.text());
                }
            }

            for (int i = 0; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    if (!columns.isEmpty()) {
                        NFLPlayerGameLogDto playerGameLog = NFLPlayerMapperUtils.mapNFLPlayerGameLogDto(columns,
                                statsList);
                        playerGameLog.setPlayoffGame(false);
                        playerGameLogs.add(playerGameLog);
                    }
                }
            }

            if (tables.size() > 1) {
                gamesList = tables.get(1).getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);
                tableHeaders = document.getElementsByTag("table").get(1).getElementsByTag("thead").get(0)
                        .getElementsByTag("tr").get(0).getElementsByTag("th");
                statsList.clear();
                for (Element header : tableHeaders) {
                    if (!header.text().isBlank()) {
                        statsList.add(header.text());
                    }
                }
                for (int i = 0; i < gamesList.size(); i++) {
                    if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                        Elements columns = gamesList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                        if (!columns.isEmpty()) {
                            NFLPlayerGameLogDto playerGameLog = NFLPlayerMapperUtils.mapNFLPlayerGameLogDto(columns,
                                    statsList);
                            playerGameLog.setPlayoffGame(true);
                            playerGameLogs.add(playerGameLog);
                        }
                    }
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (sortCron)
            playerGameLogs.sort(Comparator.comparing(PlayerGameLogDto::getDate).reversed());

        return playerGameLogs;
    }

    @Override
    public List<PlayerAdvancedGameLogDto> getPlayerAdvancedGameLogForYear(String player, int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PlayerGameLogDto> getPlayerGameLogVsTeam(String player, String team, int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TeamPerGameDto> getTeamPerGameStats(int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPlayerPictureUrl(String playerName) {
        String url = "https://www.pro-football-reference.com/players/" + playerName.charAt(0) + "/"
                + playerName + ".htm";
        try {
            Document document = getDocumentForURL(url);
            Element pictureElement = document.select(ScrapingConstants.IMG_ELEMENT).get(0);
            System.out.println(pictureElement);
            if (pictureElement != null) {
                return pictureElement.attr(ScrapingConstants.SRC);
            } else {
                System.out.println("Picture element is null");
            }
        } catch (ScrapingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "";
    }

    @Override
    public Document getDocumentForURL(String url) throws ScrapingException {
        Document doc = null;
        Connection connection = Jsoup.connect(url);

        try {
            doc = connection.get();
        } catch (IOException ex) {
            throw new ScrapingException(
                    String.format("Unsuccessful respone calling =%s Exception=%s", url,
                            ex.getMessage()));
        }

        return doc;
    }

    // get all players and their fantasy stats
    @Cacheable(value = "allNFLPlayersFantasyStats")
    public List<NFLPlayerFantasyStatsDto> getAllNFLPlayersFantasyStats(int year) {
        List<NFLPlayerFantasyStatsDto> fantasyStatsDtos = new ArrayList<>();
        String url = "https://www.pro-football-reference.com/years/" + year + "/fantasy.htm";

        try {
            Document document = Jsoup.connect(url).get();
            Elements playersList = document.getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

            int playerRanking = 1;
            for (int i = 2; i < playersList.size(); i++) {
                if (!StringUtils.isEmpty(playersList.get(i).text())) {
                    Elements playerAttributes = playersList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    if (!playerAttributes.isEmpty()
                            && NFLPlayerMapperUtils.getNFLPlayerFantasyPosition(playerAttributes) != null) {
                        fantasyStatsDtos
                                .add(NFLPlayerMapperUtils.mapNFLPlayerFantasyStatsRow(playerAttributes, playerRanking));
                        playerRanking++;
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fantasyStatsDtos;
    }

    // get player fantasy stats by player name
    public NFLPlayerFantasyStatsDto getNFLPlayerFantasyStats(String playerName, int year) {
        Optional<NFLPlayerFantasyStatsDto> playerFantasyStats = getAllNFLPlayersFantasyStats(year).stream()
                .filter(player -> player.getPlayerName().equalsIgnoreCase(playerName)).findFirst();
        return playerFantasyStats.isPresent() ? playerFantasyStats.get() : null;
    }

    private void setPlayerPositionAverageStats(NFLPlayerProfileDto player) {
        List<PlayerGameLogDto> playerGameLog = player.getGameLog();

        String playerPosition = player.getPosition();
        int yards = 0;
        int touchdowns = 0;

        if (playerPosition.equalsIgnoreCase("QB")) {
            int interceptions = 0;

            for (PlayerGameLogDto gamelog : playerGameLog) {
                NFLPlayerGameLogDto game = (NFLPlayerGameLogDto) gamelog;
                PassingStatsDto passingStats = game.getPassingStats();
                if (passingStats != null) {
                    yards += passingStats.getPassingYards();
                    touchdowns += passingStats.getPassingTouchdowns();
                    interceptions += passingStats.getInterceptions();
                }
            }

            player.setYards(yards);
            player.setTouchdowns(touchdowns);
            player.setInterceptions(interceptions);

        } else if (playerPosition.equalsIgnoreCase("RB")) {
            int rushingAttempts = 0;

            for (PlayerGameLogDto gamelog : playerGameLog) {
                NFLPlayerGameLogDto game = (NFLPlayerGameLogDto) gamelog;
                RushingStatsDto rushingStats = game.getRushingStats();
                if (rushingStats != null) {
                    rushingAttempts += rushingStats.getRushingAttempts();
                    yards += rushingStats.getRushingYards();
                    touchdowns += rushingStats.getRushingTouchdowns();
                }
            }

            player.setRushingAttempts(rushingAttempts);
            player.setYards(yards);
            player.setTouchdowns(touchdowns);

        } else {
            int receptions = 0;

            for (PlayerGameLogDto gamelog : playerGameLog) {
                NFLPlayerGameLogDto game = (NFLPlayerGameLogDto) gamelog;
                ReceivingStatsDto receivingStats = game.getReceivingStats();
                if (receivingStats != null) {
                    receptions += receivingStats.getReceptions();
                    yards += receivingStats.getReceivingYards();
                    touchdowns += receivingStats.getReceivingTouchdowns();
                }
            }

            player.setReceptions(receptions);
            player.setYards(yards);
            player.setTouchdowns(touchdowns);

        }

    }
}
