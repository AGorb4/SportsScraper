package com.sports.scraper.api.service.scraper.nfl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsDto;
import com.sports.scraper.domain.player.nfl.gamelog.NFLPlayerGameLogDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

@Service("nflScraperServiceImpl")
public class NFLScraperServiceImpl implements ScraperService {

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

            Elements gamesList = tables.get(0).getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

            for (int i = 0; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    if (!columns.isEmpty()) {
                        NFLPlayerGameLogDto playerGameLog = NFLPlayerMapperUtils.mapNFLPlayerGameLogDto(columns,
                                player.getPosition());
                        playerGameLog.setPlayoffGame(false);
                        playerGameLogs.add(playerGameLog);
                    }
                }
            }

            if (tables.size() > 1) {
                gamesList = tables.get(1).getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

                for (int i = 0; i < gamesList.size(); i++) {
                    if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                        Elements columns = gamesList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                        if (!columns.isEmpty()) {
                            NFLPlayerGameLogDto playerGameLog = NFLPlayerMapperUtils.mapNFLPlayerGameLogDto(columns,
                                    player.getPosition());
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
        // TODO Auto-generated method stub
        return null;
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
}
