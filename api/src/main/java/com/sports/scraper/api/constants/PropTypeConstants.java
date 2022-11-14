package com.sports.scraper.api.constants;

public class PropTypeConstants {
    private PropTypeConstants() {
        throw new IllegalStateException("Constants class");
    }

    // nba
    public static final String POINTS = "pts";
    public static final String THREES = "3pt";
    public static final String ASSISTS = "ast";
    public static final String REBOUNDS = "reb";
    public static final String STEALS = "stl";
    public static final String BLOCKS = "blk";
    public static final String TURNOVERS = "to";
    public static final String POINTS_REBOUNDS_ASSISTS = "ptsRebAst";
    public static final String POINTS_REBOUNDS = "ptsReb";
    public static final String POINTS_ASSISTS = "ptsAst";
    public static final String ASSISTS_REBOUNDS = "astsReb";
    public static final String STEALS_BLOCKS = "stlsBlk";

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
