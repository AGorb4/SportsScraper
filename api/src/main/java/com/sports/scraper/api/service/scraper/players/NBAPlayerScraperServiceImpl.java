package com.sports.scraper.api.service.scraper.players;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.sports.scraper.api.constants.ScrapingConstants;
import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.api.service.scraper.ScraperService;
import com.sports.scraper.api.utils.NBAPlayerMapperUtils;
import com.sports.scraper.api.utils.URLUtils;
import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.player.nba.NBAPlayerGameLogDto;
import com.sports.scraper.domain.teams.nba.NBATeamPerGame;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("nbaPlayerScraperServiceImpl")
@Slf4j
public class NBAPlayerScraperServiceImpl implements PlayerScraperService {

    private ScraperService scraperService;

    public NBAPlayerScraperServiceImpl(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @Override
    public List<PlayerPerGameStatsDto> getPlayersPerGameForSeason(int year, int pageSize) {
        List<PlayerPerGameStatsDto> responseDtos = new ArrayList<>();
        try {

            String url = URLUtils.SCRAPING_NBA_URL + "/leagues/NBA_" + year + "_per_game.html";
            log.info("Calling getPlayersPerGameForSeason URL: " + url);
            Document document = scraperService.getDocumentForURL(url);

            // only one table on this page so can just get all table rows
            Elements playersList = document.getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

            for (int i = 1; i < (pageSize > 0 ? pageSize : playersList.size()); i++) {
                if (!StringUtils.isEmpty(playersList.get(i).text())) {
                    // get the columns for the row
                    Elements playerAttributes = playersList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    // some of the rows are headings so this ignores them
                    if (!playerAttributes.isEmpty()) {
                        responseDtos.add(NBAPlayerMapperUtils.mapPlayerPerGameStatsRow(playerAttributes));
                    }
                }
            }
        } catch (ScrapingException ex) {
            log.error("Error calling getPlayersPerGameForSeason", ex);
        }
        return responseDtos;
    }

    @Override
    @Cacheable(value = "nbaPlayerGameLogForYear")
    public List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year, boolean sortCron) {
        List<PlayerGameLogDto> responseDtos = new ArrayList<>();
        try {

            String url = URLUtils.constructScrapingUrlGameLogByPlayerByYear("NBA", player, year);
            log.info("Calling getPlayerGameLogForYear URL: " + url);
            Document document = scraperService.getDocumentForURL(url);
            Elements tables = document.getElementsByTag(ScrapingConstants.TABLE_BODY_TAG);
            Elements gamesList = tables.get(tables.size() - 1).getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

            for (int i = 0; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    if (!columns.isEmpty()) {
                        NBAPlayerGameLogDto playerGameLog = NBAPlayerMapperUtils.mapNBAPlayerGameLogRow(columns);
                        playerGameLog.setPlayoffGame(false);
                        responseDtos.add(playerGameLog);
                    }
                }
            }

            if (hasPlayoffGames(document)) {
                Elements playoffGamelogElements = getPlayerPlayoffGamelogElements(document);
                for (int i = 0; i < playoffGamelogElements.size(); i++) {
                    if (!StringUtils.isEmpty(playoffGamelogElements.get(i).text())) {
                        Elements columns = playoffGamelogElements.get(i)
                                .getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                        if (!columns.isEmpty()) {
                            NBAPlayerGameLogDto playerGameLog = NBAPlayerMapperUtils.mapNBAPlayerGameLogRow(columns);
                            playerGameLog.setPlayoffGame(true);
                            responseDtos.add(playerGameLog);
                        }
                    }
                }
            }
        } catch (ScrapingException ex) {
            log.error("Error calling getPlayerGameLogForYear", ex);
        }

        if (sortCron)
            responseDtos.sort(Comparator.comparing(PlayerGameLogDto::getDate).reversed());

        return responseDtos;
    }

    @Override
    public List<PlayerAdvancedGameLogDto> getPlayerAdvancedGameLogForYear(String player, int year) {
        List<PlayerAdvancedGameLogDto> responseDtos = new ArrayList<>();
        try {

            String url = URLUtils.SCRAPING_NBA_URL + "/players/" + player.charAt(0) + "/" + player
                    + "/gamelog-advanced/" + year;
            log.info("Calling getPlayerAdvancedGameLogForYear URL: " + url);
            Document document = scraperService.getDocumentForURL(url);
            Elements tables = document.getElementsByTag(ScrapingConstants.TABLE_BODY_TAG);
            Elements gamesList = tables.get(tables.size() - 1).getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

            for (int i = 1; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    if (!columns.isEmpty()) {
                        responseDtos.add(NBAPlayerMapperUtils.mapPlayerAdvancedGameLogRow(columns));
                    }
                }
            }
        } catch (ScrapingException ex) {
            log.error("Error calling getPlayerAdvancedGameLogForYear", ex);
        }
        return responseDtos;
    }

    @Cacheable(value = "nbaTeams", key = "#year")
    public List<NBATeamPerGame> getTeamPerGameStats(int year) {
        List<NBATeamPerGame> responseDtos = new ArrayList<>();
        try {

            String url = URLUtils.SCRAPING_NBA_URL + "/leagues/NBA_" + year + ".html";
            log.info("Calling getTeamPerGameStats URL: " + url);
            Document document = scraperService.getDocumentForURL(url);
            Element table = document.getElementById("per_game-team");
            Elements teamsList = table.getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);

            for (int i = 1; i < teamsList.size(); i++) {
                if (!StringUtils.isEmpty(teamsList.get(i).text())) {
                    Elements columns = teamsList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    if (!columns.isEmpty()) {
                        responseDtos.add(NBAPlayerMapperUtils.mapTeamPerGameStatsRow(columns));
                    }
                }
            }

            Element tableFooter = table.getElementsByTag(ScrapingConstants.TABLE_FOOTER).get(0);
            Element footerRow = tableFooter.getElementsByTag(ScrapingConstants.TABLE_ROW_TAG).get(0);
            responseDtos.add(
                    NBAPlayerMapperUtils
                            .mapTeamPerGameStatsRow(footerRow.getElementsByTag(ScrapingConstants.TABLE_DATA_TAG)));
        } catch (ScrapingException ex) {
            log.error("Error calling getTeamPerGameStats", ex);
        }
        return responseDtos;
    }

    @Override
    public List<PlayerGameLogDto> getPlayerGameLogVsTeam(String player, String vsTeam, int year) {
        if (vsTeam == null || StringUtils.isBlank(vsTeam)) {
            log.error("getPlayerGameLogVsTeam VS team cannot be null or empty");
        }

        return getPlayerGameLogForYear(player, year, false).stream()
                .filter(gl -> gl.getOpponent().equalsIgnoreCase(vsTeam))
                .collect(Collectors.toList());
    }

    @Override
    public PlayerPerGameStatsDto getPlayerPerGameForSeason(String playerName, int year) {
        log.info("getPlayerPerGameForSeason");
        List<PlayerPerGameStatsDto> playersPerGameList = getPlayersPerGameForSeason(year, 0);

        List<PlayerPerGameStatsDto> playerPerGameList = playersPerGameList.stream()
                .filter(player -> player.getPlayerName().equalsIgnoreCase(playerName))
                .collect(Collectors.toList());

        // if more than 1 result, means they were trading and need to find where
        // team:"TOT" record
        if (playerPerGameList.size() > 1) {
            return playerPerGameList.stream().filter(player -> player.getTeam().equals("TOT"))
                    .collect(Collectors.toList()).get(0);
        }

        return playerPerGameList.get(0);
    }

    private boolean hasPlayoffGames(Document document) {
        document = Jsoup.parse(document.toString().replaceAll(ScrapingConstants.COMMENT_REGEX, ""));
        Elements content = document.select(ScrapingConstants.DIV_CONTENT);
        Elements nestedDivs = content.select(ScrapingConstants.DIV);
        Elements playoffGameLogHeaderElement = nestedDivs.get(29).select(ScrapingConstants.H2);
        if (!playoffGameLogHeaderElement.isEmpty()) {
            String headerText = playoffGameLogHeaderElement.get(0).text();
            return headerText.toLowerCase().contains("playoff");
        }
        return false;
    }

    private Elements getPlayerPlayoffGamelogElements(Element document) {
        document = Jsoup.parse(document.toString().replaceAll(ScrapingConstants.COMMENT_REGEX, ""));
        Elements content = document.select(ScrapingConstants.DIV_CONTENT);
        Elements nestedDivs = content.select(ScrapingConstants.DIV);
        return nestedDivs.get(29).select(ScrapingConstants.TABLE_BODY_TAG).select(ScrapingConstants.TABLE_ROW_TAG);
    }
}
