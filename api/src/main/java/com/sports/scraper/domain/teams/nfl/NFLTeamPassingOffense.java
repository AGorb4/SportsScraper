package com.sports.scraper.domain.teams.nfl;

import lombok.Data;

@Data
public class NFLTeamPassingOffense {
    private int rank;
    private int completions;
    private int attempts;
    private Double completionPercentage;
    private int yards;
    private int touchdowns;
    private int touchdownPercentage;
    private int interceptions;
    private int interceptionPercentage;
    private int longestPass;
    private Double yardsPerAttempt;
    private Double adjustedYardsPerAttempt;
    private Double yardsPerCompletion;
    private Double yardsPerGame;
    private Double rating;
    private int sacks;
    private int sackYards;
    private Double sackPercentage;
    private Double netYardsPerAttempt;
    private Double adjustedNetYardsPerAttempt;
    private int fourthQuaterComebacks;
    private int gameWinningDrives;
    private Double expectedPoints;

}
