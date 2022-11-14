package com.sports.scraper.api.constants;

public class DraftKingsConstants {

    private DraftKingsConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String DRAFTKINGS_NBA_EVENTS_URL = "https://sportsbook.draftkings.com//sites/US-SB/api/v4/eventgroups/88670846";
    public static final String DRAFTKINGS_NFL_EVENTS_URL = "https://sportsbook.draftkings.com//sites/US-NJ-SB/api/v4/eventgroups/88670561";
}
