package com.sports.scraper.domain.player.nfl.gamelog;

import com.sports.scraper.domain.player.PlayerGameLogDto;

import lombok.Data;

@Data
public class NFLPlayerGameLogDto extends PlayerGameLogDto {

    public NFLPlayerGameLogDto() {
        super();
    }

    private int week;

    // scoring
    private int touchdowns;
    private int pointsScored;

    // tackles
    private int sacks;
    private int soloTackles;
    private int assistedTackles;
    private int tacklesCombo;
    private int tacklesForLoss;
    private int qbHits;

    // fumbles
    private int fumbles;
    private int fumblesLost;
    private int fumblesForced;
    private int fumblesRecovered;
    private int fumbleYards;
    private int fumblesTouchdowns;

    // offensive snaps
    private int offSnapsNum;
    private String offSnapsPct;

    // defensive snaps
    private int defSnapsNum;
    private String defSnapsPct;

    // special teams snaps
    private int stSnapsNum;
    private String stSnapsPct;

}
