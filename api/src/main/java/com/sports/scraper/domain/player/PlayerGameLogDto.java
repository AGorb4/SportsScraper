package com.sports.scraper.domain.player;

import lombok.Data;

@Data
public class PlayerGameLogDto {

    private int rank;
    private int gameCount;
    private String date;
    private String age;
    private String team;
    private boolean isAway;
    private String opponent;
    private String result;
    private String reason;
    private boolean startedGame;
    private String minutesPlayed;
    private int fieldGoals;
    private int fieldGoalAttempts;
    private double fieldGoalPercentage;
    private int threePointers;
    private int threePointerAttempts;
    private double threePointerPercentage;
    private int freeThrows;
    private int freeThrowAttempts;
    private double freeThrowPercentage;
    private int offensiveRebounds;
    private int defensiveRebounds;
    private int totalRebounds;
    private int assists;
    private int steals;
    private int blocks;
    private int turnovers;
    private int personalFouls;
    private int points;
    private double gameScore;
    private String plusMinus;

}
