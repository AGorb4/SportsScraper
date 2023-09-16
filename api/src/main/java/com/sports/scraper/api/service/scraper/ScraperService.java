package com.sports.scraper.api.service.scraper;

import com.sports.scraper.api.exceptions.ScrapingException;

import org.jsoup.nodes.Document;

public interface ScraperService {

    Document getDocumentForURL(String url) throws ScrapingException;

    String getPlayerPictureUrl(String league, String playerName);

}
