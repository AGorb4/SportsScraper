package com.sports.scraper.api.service.scraper.nba;

import java.io.IOException;
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
import com.sports.scraper.domain.team.TeamPerGameDto;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("nbaScraperServiceImpl")
public class NBAScraperServiceImpl implements ScraperService {

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

    @Override
    public List<PlayerPerGameStatsDto> getPlayersPerGameForSeason(int year, int pageSize) {
        List<PlayerPerGameStatsDto> responseDtos = new ArrayList<>();
        try {

            String url = URLUtils.SCRAPING_NBA_URL + "/leagues/NBA_" + year + "_per_game.html";
            System.out.println(url);
            Document document = Jsoup.connect(url).get();
            Elements playersList = document.getElementsByTag(ScrapingConstants.TABLE_ROW_TAG);
            System.out.println(playersList.size());
            for (int i = 1; i < (pageSize > 0 ? pageSize : playersList.size()); i++) {
                if (!StringUtils.isEmpty(playersList.get(i).text())) {
                    Elements playerAttributes = playersList.get(i).getElementsByTag(ScrapingConstants.TABLE_DATA_TAG);
                    if (!playerAttributes.isEmpty()) {
                        responseDtos.add(NBAPlayerMapperUtils.mapPlayerPerGameStatsRow(playerAttributes));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseDtos;
    }

    @Override
    @Cacheable(value = "nbaPlayerGameLogForYear")
    public List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year, boolean sortCron) {
        System.out.println("Getting NBA player game log for " + player + " year " + year);
        List<PlayerGameLogDto> responseDtos = new ArrayList<>();
        try {

            String url = URLUtils.constructScrapingUrlGameLogByPlayerByYear("NBA", player, year);

            Document document = Jsoup.connect(url).get();
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
        } catch (IOException ex) {
            ex.printStackTrace();
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

            Document document = Jsoup.connect(url).get();
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseDtos;
    }

    @Override
    @Cacheable(value = "nbaTeams", key = "#year")
    public List<TeamPerGameDto> getTeamPerGameStats(int year) {
        System.out.println("Getting teams for " + year);
        List<TeamPerGameDto> responseDtos = new ArrayList<>();
        try {

            String url = URLUtils.SCRAPING_NBA_URL + "/leagues/NBA_" + year + ".html";

            Document document = Jsoup.connect(url).get();
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseDtos;
    }

    @Override
    public List<PlayerGameLogDto> getPlayerGameLogVsTeam(String player, String vsTeam, int year) {
        if (vsTeam == null || vsTeam.isBlank()) {
            System.out.println("getPlayerGameLogVsTeam VS team cannot be null or empty");
        }

        return getPlayerGameLogForYear(player, year, false).stream()
                .filter(gl -> gl.getOpponent().equalsIgnoreCase(vsTeam))
                .collect(Collectors.toList());
    }

    @Override
    public PlayerPerGameStatsDto getPlayerPerGameForSeason(String playerName, int year) {
        System.out.println("getPlayerPerGameForSeason");
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
        Elements playoffGameLog = nestedDivs.get(28).select(ScrapingConstants.TABLE_BODY_TAG);
        return !playoffGameLog.isEmpty();
    }

    private Elements getPlayerPlayoffGamelogElements(Element document) {
        document = Jsoup.parse(document.toString().replaceAll(ScrapingConstants.COMMENT_REGEX, ""));
        Elements content = document.select(ScrapingConstants.DIV_CONTENT);
        Elements nestedDivs = content.select(ScrapingConstants.DIV);
        return nestedDivs.get(28).select(ScrapingConstants.TABLE_BODY_TAG).select(ScrapingConstants.TABLE_ROW_TAG);
    }

    public String getPlayerPictureUrl(String playerSystemName) {
        String url = "https://www.basketball-reference.com/players/" + playerSystemName.charAt(0) + "/"
                + playerSystemName + ".html";
        try {
            Document document = getDocumentForURL(url);
            Element pictureElement = document.select(ScrapingConstants.IMG_ELEMENT).get(0);
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
}
