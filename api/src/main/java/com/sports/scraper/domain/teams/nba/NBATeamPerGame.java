package com.sports.scraper.domain.teams.nba;

import lombok.Data;

@Data
public class NBATeamPerGame {
    private String teamName;
    private String teamUrl;
    private int gamesPlayed;
    private double minutesPlayed;
    private double fieldGoals;
    private double fieldGoalAttempts;
    private double fieldGoalPercentage;
    private double threePointers;
    private double threePointerAttempts;
    private double threePointerPercentage;
    private double twoPointers;
    private double twoPointerAttempts;
    private double twoPointerPercentage;
    private double freeThrows;
    private double freeThrowAttempts;
    private double freeThrowPercentage;
    private double offensiveRebounds;
    private double defensiveRebounds;
    private double totalRebounds;
    private double assists;
    private double steals;
    private double blocks;
    private double turnovers;
    private double personalFouls;
    private double points;
}