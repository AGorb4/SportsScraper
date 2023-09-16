package com.sports.scraper.api.service.teams.nfl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.text.html.HTML;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sports.scraper.api.constants.ScrapingConstants;
import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.api.service.scraper.ScraperService;
import com.sports.scraper.api.utils.NFLTeamMapperUtils;
import com.sports.scraper.domain.teams.nfl.NFLTeam;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NFLTeamsServiceScraperImpl implements NFLTeamsScraperService {

    @Autowired
    private ScraperService scraperService;

    @Override
    public List<NFLTeam> getNFLTeams(int year) throws ScrapingException {
        String url = "https://www.pro-football-reference.com/years/" + year;
        log.info("getNFLTeams " + url);

        List<NFLTeam> nflTeams = new ArrayList<>();

        Document document = scraperService.getDocumentForURL(url);
        Elements allTables = document.getElementsByTag(HTML.Tag.TABLE.toString());

        Element afcTable = allTables.select("table#AFC").first();
        nflTeams.addAll(mapTableRows(afcTable));

        Element nfcTable = allTables.select("table#NFC").first();
        nflTeams.addAll(mapTableRows(nfcTable));

        nflTeams.sort(Comparator.comparing(NFLTeam::getWins).reversed());

        return nflTeams;
    }

    private List<NFLTeam> mapTableRows(Element table) {
        List<NFLTeam> nflTeams = new ArrayList<>();

        Elements tableRows = table.select(ScrapingConstants.TABLE_BODY_TAG).first()
                .select(ScrapingConstants.TABLE_ROW_TAG);

        for (Element tableRow : tableRows) {
            if (!tableRow.getElementsByTag("th").isEmpty()) {
                nflTeams.add(NFLTeamMapperUtils.mapNFLTeam(tableRow));
            }
        }

        return nflTeams;
    }

}
