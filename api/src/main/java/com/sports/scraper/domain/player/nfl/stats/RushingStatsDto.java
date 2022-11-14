package com.sports.scraper.domain.player.nfl.stats;

import lombok.Data;

@Data
public class RushingStatsDto {

    private int rushingAttempts;
    private int rushingYards;
    private String rushingYardsPerCarry;
    private int rushingTouchdowns;

}
