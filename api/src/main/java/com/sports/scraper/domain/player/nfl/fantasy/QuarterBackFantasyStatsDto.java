package com.sports.scraper.domain.player.nfl.fantasy;

import lombok.Data;

@Data
public class QuarterBackFantasyStatsDto extends NFLPlayerFantasyStatsDto {

    // passing
    private int passesCompleted;
    private int passesAttempted;
    private int passingYards;
    private int passingTouchdowns;
    private int interceptions;

    // rushing
    private int rushingAttempts;
    private int rushingYards;
    private String rushingYardsPerAttempt;
    private int rushingTouchdowns;
}
