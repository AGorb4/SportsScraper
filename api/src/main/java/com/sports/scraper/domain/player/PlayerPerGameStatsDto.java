package com.sports.scraper.domain.player;

import lombok.Data;

@Data
public class PlayerPerGameStatsDto {

    private String playerName;
    private String systemName;
    private String playerUrl;
    private String position;
    private int age;
    private String team;
    private int gamesCount;
    private int gamesStartedCount;
    private float minutesPlayed;
    private float fieldGoals;
    private float fieldGoalAttempts;
    private double fieldGoalPercentage;
    private float threePointers;
    private float threePointerAttempts;
    private double threePointerPercentage;
    private float twoPointers;
    private float twoPointerAttempts;
    private double twoPointerPercentage;
    private double effectiveFieldGoalPercentage;
    private float freeThrows;
    private float freeThrowAttempts;
    private double freeThrowPercentage;
    private float offensiveRebounds;
    private float defensiveRebounds;
    private float totalRebounds;
    private float assists;
    private float steals;
    private float blocks;
    private float turnovers;
    private float personalFouls;
    private float points;

}
