package com.sports.scraper.api.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.sports.scraper.api.constants.DraftKingsConstants;

public class URLUtils {

    public static final String SCRAPING_NBA_URL = "https://www.basketball-reference.com/";

    public static final String SCRAPING_NFL_URL = "https://www.pro-football-reference.com/";

    private static final String NBA = "NBA";
    private static final String NFL = "NFL";

    private static Map<String, String> draftkingsUrlMap = null;

    private URLUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, String> getDraftkingsUrlMap() {
        if (draftkingsUrlMap == null) {
            createDraftkingsUrlMap();
            return draftkingsUrlMap;
        }
        return draftkingsUrlMap;
    }

    @PostConstruct
    private static void createDraftkingsUrlMap() {
        System.out.println("Create Draftkings URL Map");
        draftkingsUrlMap = new HashMap<>();
        draftkingsUrlMap.put(NBA, DraftKingsConstants.DRAFTKINGS_NFL_EVENTS_URL);
        draftkingsUrlMap.put(NFL, DraftKingsConstants.DRAFTKINGS_NBA_EVENTS_URL);
    }

    public static String getScrapingUrlByLeague(String league) {
        switch (league) {
            case NBA:
                return SCRAPING_NBA_URL;
            case NFL:
                return SCRAPING_NFL_URL;
            default:
                return "";
        }
    }

    public static String constructScrapingUrlGameLogByPlayerByYear(String league, String playerSystemName, int year) {
        StringBuilder sb = new StringBuilder();
        sb.append(getScrapingUrlByLeague(league));
        sb.append("/players");
        sb.append("/" + playerSystemName.charAt(0));
        sb.append("/" + playerSystemName);
        sb.append("/gamelog");
        sb.append("/" + year);
        return sb.toString();
    }
}
