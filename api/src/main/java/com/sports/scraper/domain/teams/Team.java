package com.sports.scraper.domain.teams;

import lombok.Data;

@Data
public class Team {

    private String name;
    private String url;
    private int gamesPlayed;
}
