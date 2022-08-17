package com.sports.scraper.domain.player;

import lombok.Data;

@Data
public class PlayerGameLogDto {

    private int rank;
    private int gameCount;
    private String date;
    private boolean startedGame;
    private String age;
    private String team;
    private boolean isAway;
    private String opponent;
    private String result;
    private String reason;

}
