package com.sports.scraper.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sports.scraper.api.constants.ScrapingConstants;
import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.api.utils.MapperUtils;
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
    public List<PlayerPerGameStatsDto> getPlayerPerGameForSeason(int year, int pageSize) {
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
    public List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year) {
        List<PlayerGameLogDto> responseDtos = new ArrayList<>();
        try {

            String url = ScrapingConstants.BASE_URL + "/players/" + player.charAt(0) + "/" + player + "/gamelog/"
                    + year;

            Document document = Jsoup.connect(url).get();
            Elements tables = document.getElementsByTag("tbody");
            Elements gamesList = tables.get(tables.size() - 1).getElementsByTag("tr");

            for (int i = 1; i < gamesList.size(); i++) {
                if (!StringUtils.isEmpty(gamesList.get(i).text())) {
                    Elements columns = gamesList.get(i).getElementsByTag("td");
                    if (!columns.isEmpty()) {
                        responseDtos.add(MapperUtils.mapPlayerGameLogRow(columns));
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
}
