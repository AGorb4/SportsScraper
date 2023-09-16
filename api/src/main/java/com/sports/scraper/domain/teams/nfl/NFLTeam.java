package com.sports.scraper.domain.teams.nfl;

import com.sports.scraper.domain.teams.Team;

import lombok.Data;

@Data
public class NFLTeam extends Team {

    private int wins;
    private int loses;
    private int ties;
    private Number winLossPercentage;
    private int pointsFor;
    private int pointsAgainst;
    private int pointsDifferential;
    private Number marginOfVictory;
    private Number strengthOfSchedule;
    private Number simpleRatingSystem;
    private Number offenseSimpleRatingSystem;
    private Number defenseSimpleRatingSystem;
    private NFLTeamOffense nflTeamOffense;
}
