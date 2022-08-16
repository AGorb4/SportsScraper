package com.sports.scraper.domain.player.nba;

import com.sports.scraper.domain.player.PlayerGameLogDto;

import lombok.Data;

@Data
public class NBAPlayerGameLogDto extends PlayerGameLogDto {

    public NBAPlayerGameLogDto() {
        super();
    }

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
