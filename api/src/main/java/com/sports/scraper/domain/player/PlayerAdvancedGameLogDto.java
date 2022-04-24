package com.sports.scraper.domain.player;

import lombok.Data;

@Data
public class PlayerAdvancedGameLogDto {

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
    private String trueShootingPercent;
    private String effectiveFieldGoalPercent;
    private String offensiveReboundPercent;
    private String defensiveReboundPercent;
    private String totalReboundPercent;
    private String assistPercent;
    private String stealPercent;
    private String blockPercent;
    private String turnoverPercent;
    private String usagePercent;
    private int offensiveRating;
    private int defenseRating;
    private String gameScore;
    private String boxPlusMinus;
}
