package com.sports.scraper.api.constants;

public class PropTypeConstants {
    private PropTypeConstants() {
        throw new IllegalStateException("Constants class");
    }

    // nba
    public static final String POINTS = "points";
    public static final String THREES = "threes";
    public static final String ASSISTS = "assists";
    public static final String REBOUNDS = "rebounds";
    public static final String STEALS = "steals";
    public static final String BLOCKS = "blocks";
    public static final String TURNOVERS = "turnovers";
    public static final String POINTS_REBOUNDS_ASSISTS = "ptsRebAst";
    public static final String POINTS_REBOUNDS = "ptsReb";
    public static final String POINTS_ASSISTS = "ptsAst";
    public static final String ASSISTS_REBOUNDS = "astReb";
    public static final String STEALS_BLOCKS = "stlBlk";

    // nfl
    public static final String PASSING_YARDS = "passingYards";
    public static final String RUSHING_YARDS = "rushingYards";
    public static final String RECEIVING_YARDS = "receivingYards";

    public static final String PASSING_ATTEMPTS = "passingAttempts";
    public static final String PASSING_COMPLETIONS = "passingCompletions";
    public static final String INTERCEPTIONS = "interceptions";
    public static final String TARGETS = "targets";
    public static final String RECEPTIONS = "receptions";
    public static final String CARRIES = "carries";

    public static final String TOUCHDOWNS = "touchdowns";
    public static final String PASSING_TOUCHDOWNS = "passingTouchdowns";
    public static final String RUSHING_TOUCHDOWNS = "rushingTouchdowns";
    public static final String RECEIVING_TOUCHDOWNS = "receivingTouchdowns";
}
