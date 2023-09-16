package com.sports.scraper.api.utils;

import java.text.DecimalFormat;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sports.scraper.api.constants.ScrapingConstants;
import com.sports.scraper.domain.teams.nfl.NFLTeam;

public class NFLTeamMapperUtils {

    private NFLTeamMapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static NFLTeam mapNFLTeam(Element row) {
        NFLTeam nflTeam = new NFLTeam();

        Element nameHeader = row.select(ScrapingConstants.TABLE_HEADER_TAG).first();

        StringBuilder nameSb = new StringBuilder(nameHeader.text());
        char lastChar = nameSb.charAt(nameSb.length() - 1);

        if (lastChar == '*' || lastChar == '+') {
            nameSb.deleteCharAt(nameSb.length() - 1);
        }

        nflTeam.setName(nameSb.toString());
        nflTeam.setUrl("https://www.pro-football-reference.com/" + MapperUtils.getElementUrl(nameHeader));

        Elements rowData = row.select("td");
        nflTeam.setWins(Integer.parseInt(rowData.get(0).text()));
        nflTeam.setLoses(Integer.parseInt(rowData.get(1).text()));
        nflTeam.setTies(Integer.parseInt(rowData.get(2).text()));
        nflTeam.setGamesPlayed(nflTeam.getWins() + nflTeam.getLoses() + nflTeam.getTies());
        nflTeam.setWinLossPercentage(NumberUtils.createNumber(rowData.get(3).text()));
        nflTeam.setPointsFor(Integer.parseInt(rowData.get(4).text()));
        nflTeam.setPointsAgainst(Integer.parseInt(rowData.get(5).text()));
        nflTeam.setPointsDifferential(Integer.parseInt(rowData.get(6).text()));
        nflTeam.setMarginOfVictory(NumberUtils.createNumber(rowData.get(7).text()));
        nflTeam.setStrengthOfSchedule(NumberUtils.createNumber(rowData.get(8).text()));
        nflTeam.setSimpleRatingSystem(NumberUtils.createNumber(rowData.get(9).text()));
        nflTeam.setOffenseSimpleRatingSystem(NumberUtils.createNumber(rowData.get(10).text()));
        nflTeam.setDefenseSimpleRatingSystem(NumberUtils.createNumber(rowData.get(11).text()));

        return nflTeam;
    }
}
