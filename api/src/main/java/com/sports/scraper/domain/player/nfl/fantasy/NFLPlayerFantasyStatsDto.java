package com.sports.scraper.domain.player.nfl.fantasy;

import lombok.Data;

@Data
public class NFLPlayerFantasyStatsDto {

    private int rank;
    private String playerName;
    private String playerSystemName;
    private String playerUrl;
    private String team;
    private String position;
    private int age;

    // games
    private int gamesPlayed;
    private int gamesStarted;

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

    // receiving
    private int targets;
    private int receptions;
    private int receivingYards;
    private String receivingYardsPerReception;
    private int receivingTouchdowns;

    // fumbles
    private int fumbles;
    private int fumblesLost;

    // scoring
    private int touchdowns;
    private int twoPointConversionMade;
    private int twoPointConversionPasses;

    // fantasy
    private int fantasyPoints;
    private String fantasyPointsPpr;
    private int fantasyVbd;
    private int fantasyPositionRank;
    private int fantasyOverallRank;
}
