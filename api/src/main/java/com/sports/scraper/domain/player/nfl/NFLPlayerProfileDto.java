package com.sports.scraper.domain.player.nfl;

import java.util.List;

import com.sports.scraper.domain.player.PlayerGameLogDto;

import lombok.Data;

@Data
public class NFLPlayerProfileDto {
    private String playerName;
    private String systemName;
    private String playerUrl;
    private String playerPictureUrl;
    private String position;
    private int age;
    private String team;
    private int gamesCount;
    private int gamesStartedCount;

    // qb
    private String completionPercentage;
    private int interceptions;

    // rb
    private int rushingAttempts;

    // receiver
    private int receptions;

    private int yards;
    private String yardsPerAttempt;
    private int touchdowns;

    private List<PlayerGameLogDto> gameLog;
}
