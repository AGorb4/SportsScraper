package com.sports.scraper.domain.player.nfl.stats;

import lombok.Data;

@Data
public class PassingStatsDto {

    private int passesAttempted;
    private int passesCompleted;
    private String completionPercentage;
    private int passingYards;
    private int passingTouchdowns;
    private int interceptions;
    private String passerRating;
    private String passingYardsPerAttempt;
    private String airYardsPerAttempt;
    private String sacks;
    private int sackYards;

}
