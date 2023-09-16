package com.sports.scraper.domain.teams.nfl;

import lombok.Data;

@Data
public class NFLTeamOffense {
    private int offensiveRank;
    private int pointsFor;
    private int yards;
    private int plays;
    private Double yardsPerPlay;
    private int turnovers;
    private int fumblesLost;
    private int firstDowns;
    private NFLTeamPassingOffense passingOffense;
}
