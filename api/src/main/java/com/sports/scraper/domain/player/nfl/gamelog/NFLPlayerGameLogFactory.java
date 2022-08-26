package com.sports.scraper.domain.player.nfl.gamelog;

public class NFLPlayerGameLogFactory {

    public static NFLPlayerGameLogDto createNFLPlayerGameLog(String position) {
        if (position == null)
            return null;

        if (position.equalsIgnoreCase("RB")) {
            return new NFLPlayerGameLogDto();
        } else if (position.equalsIgnoreCase("WR")) {
            return new WideReceiverGameLogDto();
        } else if (position.equalsIgnoreCase("TE")) {
            return new NFLPlayerGameLogDto();
        } else if (position.equalsIgnoreCase("QB")) {
            return new NFLPlayerGameLogDto();
        } else {
            return null;
        }
    }
}
