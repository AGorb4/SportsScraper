package com.sports.scraper.domain.player.nfl.stats;

import lombok.Data;

@Data
public class FumbleStatsDto {
    private int fumbles;
    private int fumblesLost;
    private int fumblesForced;
    private int fumblesRecovered;
    private String fumbleYards;
    private int fumblesTouchdowns;
}
