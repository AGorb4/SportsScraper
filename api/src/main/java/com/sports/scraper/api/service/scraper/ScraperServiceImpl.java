package com.sports.scraper.api.service.scraper;

import java.io.IOException;

import com.sports.scraper.api.constants.ScrapingConstants;
import com.sports.scraper.api.exceptions.ScrapingException;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScraperServiceImpl implements ScraperService {

    @Override
    public Document getDocumentForURL(String url) throws ScrapingException {
        log.info("Getting document for url : " + url);
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
    public String getPlayerPictureUrl(String league, String playerSystemName) {
        String url = "";

        if (league.equalsIgnoreCase("NBA")) {
            url = "https://www.basketball-reference.com/players/" + playerSystemName.charAt(0) + "/"
                    + playerSystemName + ".html";
        } else if (league.equalsIgnoreCase("NFL")) {
            url = "https://www.pro-football-reference.com/players/" + playerSystemName.charAt(0) + "/"
                    + playerSystemName + ".htm";
        }
        log.info("Calling getPlayerPictureUrl URL: " + url);
        try {
            Document document = getDocumentForURL(url);
            Element pictureElement = document.select(ScrapingConstants.IMG_ELEMENT).get(0);
            if (pictureElement != null) {
                return pictureElement.attr(ScrapingConstants.SRC);
            } else {
                log.error("Player picture element is null");
            }
        } catch (ScrapingException e) {
            log.error("Error getting player picture" + e.getMessage());
        }
        return "";
    }
}
