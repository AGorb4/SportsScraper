package com.sports.scraper.domain.props.statistics.responses;

import java.util.List;

import com.sports.scraper.domain.player.PlayerGameLogDto;

import lombok.Data;

@Data
public class PlayerPropStatisticsDto {

    private String propType;
    private float propTotal;
    private float playerAverage;
    private int gamesPlayed;
    private int gamesStarted;
    private int lastNInput;
    private String lastNRecord;
    private int lastNWinPercentage;
    private String recordAgainstTeam;
    private int againstTeamPercentage;
    private List<PlayerGameLogDto> gameLog;
}
