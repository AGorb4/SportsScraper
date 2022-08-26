package com.sports.scraper.domain.player.nfl.fantasy;

public class NFLPlayerFantasyStatsFactory {

    public static NFLPlayerFantasyStatsDto createNFLPlayer(String position) {
        if (position == null)
            return null;

        if (position.equalsIgnoreCase("RB")) {
            return new RunningBackFantasyStatsDto();
        } else if (position.equalsIgnoreCase("WR")) {
            return new WideReceiverFantasyStatsDto();
        } else if (position.equalsIgnoreCase("TE")) {
            return new TightEndFantasyStatsDto();
        } else if (position.equalsIgnoreCase("QB")) {
            return new QuarterBackFantasyStatsDto();
        } else {
            return null;
        }
    }
}
