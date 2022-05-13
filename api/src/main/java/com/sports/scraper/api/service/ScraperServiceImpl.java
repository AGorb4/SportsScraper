package com.sports.scraper.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.sports.scraper.api.constants.ScrapingConstants;
import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.api.utils.MapperUtils;
import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ScraperServiceImpl implements ScraperService {

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

            String url = ScrapingConstants.BASE_URL + "/leagues/NBA_" + year + "_per_game.html";

            Document document = Jsoup.connect(url).get();
            Elements playersList = document.getElementsByTag("tr");

            for (int i = 1; i < (pageSize > 0 ? pageSize : playersList.size()); i++) {
                if (!StringUtils.isEmpty(playersList.get(i).text())) {
                    Elements playerAttributes = playersList.get(i).getElementsByTag("td");
                    if (!playerAttributes.isEmpty()) {
                        responseDtos.add(MapperUtils.mapPlayerPerGameStatsRow(playerAttributes));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseDtos;
    }

    @Override
    @Cacheable(value = "playerGameLogForYear")
    public List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year, boolean sortCron) {
        System.out.println("Getting player game log for " + player + " year " + year);
        List<PlayerGameLogDto> responseDtos = new ArrayList<>();
        try {

            String url = ScrapingConstants.BASE_URL + "/players/" + player.charAt(0) + "/" + player + "/gamelog/"
                    + year;

            Document document = Jsoup.connect(url).get();
            Elements tables = document.getElementsByTag("tbody");
            Elements gamesList = tables.get(tables.size() - 1).getElementsByTag("tr");

            for (int i = 0; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag("td");
                    if (!columns.isEmpty()) {
                        responseDtos.add(MapperUtils.mapPlayerGameLogRow(columns));
                    }
                }
            }

            if (hasPlayoffGames(document)) {
                Elements playoffGamelogElements = getPlayerPlayoffGamelogElements(document);
                for (int i = 0; i < playoffGamelogElements.size(); i++) {
                    if (!StringUtils.isEmpty(playoffGamelogElements.get(i).text())) {
                        Elements columns = playoffGamelogElements.get(i).getElementsByTag("td");
                        if (!columns.isEmpty()) {
                            responseDtos.add(MapperUtils.mapPlayerGameLogRow(columns));
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

            String url = ScrapingConstants.BASE_URL + "/players/" + player.charAt(0) + "/" + player
                    + "/gamelog-advanced/" + year;

            Document document = Jsoup.connect(url).get();
            Elements tables = document.getElementsByTag("tbody");
            Elements gamesList = tables.get(tables.size() - 1).getElementsByTag("tr");

            for (int i = 1; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag("td");
                    if (!columns.isEmpty()) {
                        responseDtos.add(MapperUtils.mapPlayerAdvancedGameLogRow(columns));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseDtos;
    }

    @Override
    @Cacheable(value = "teams", key = "#year")
    public List<TeamPerGameDto> getTeamPerGameStats(int year) {
        System.out.println("Getting teams for " + year);
        List<TeamPerGameDto> responseDtos = new ArrayList<>();
        try {

            String url = ScrapingConstants.BASE_URL + "/leagues/NBA_" + year + ".html";

            Document document = Jsoup.connect(url).get();
            Element table = document.getElementById("per_game-team");
            Elements teamsList = table.getElementsByTag("tr");

            for (int i = 1; i < teamsList.size(); i++) {
                if (!StringUtils.isEmpty(teamsList.get(i).text())) {
                    Elements columns = teamsList.get(i).getElementsByTag("td");
                    if (!columns.isEmpty()) {
                        responseDtos.add(MapperUtils.mapTeamPerGameStatsRow(columns));
                    }
                }
            }

            Element tableFooter = table.getElementsByTag("tfoot").get(0);
            Element footerRow = tableFooter.getElementsByTag("tr").get(0);
            responseDtos.add(MapperUtils.mapTeamPerGameStatsRow(footerRow.getElementsByTag("td")));
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
        List<PlayerPerGameStatsDto> playersPerGameList = getPlayersPerGameForSeason(year, 0);

        List<PlayerPerGameStatsDto> playerPerGameList = playersPerGameList.stream()
                .filter(player -> player.getSystemName().equals(playerName))
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
        document = Jsoup.parse(document.toString().replaceAll("<!--|-->", ""));
        Elements content = document.select("div#content");
        Elements nestedDivs = content.select("div");
        Elements playoffGameLog = nestedDivs.get(28).select("tbody");
        return !playoffGameLog.isEmpty();
    }

    private Elements getPlayerPlayoffGamelogElements(Element document) {
        document = Jsoup.parse(document.toString().replaceAll("<!--|-->", ""));
        Elements content = document.select("div#content");
        Elements nestedDivs = content.select("div");
        return nestedDivs.get(28).select("tbody").select("tr");
    }
}
