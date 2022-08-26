package com.sports.scraper.domain.player.nfl.fantasy;

import lombok.Data;

@Data
public class WideReceiverFantasyStatsDto extends NFLPlayerFantasyStatsDto {

    // rushing
    private int rushingAttempts;
    private int rushingYards;
    private String rushingYardsPerAttempt;
    private int rushingTouchdowns;

    // receiving
    private int targets;
    private int receptions;
    private int receivingYards;
    private String receivingYardsPerReception;
    private int receivingTouchdowns;
}
