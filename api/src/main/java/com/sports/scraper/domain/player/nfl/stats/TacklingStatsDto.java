package com.sports.scraper.domain.player.nfl.stats;

import lombok.Data;

@Data
public class TacklingStatsDto {
    private String sacks;
    private String soloTackles;
    private int assistedTackles;
    private int tacklesCombo;
    private int tacklesForLoss;
    private int qbHits;
}
