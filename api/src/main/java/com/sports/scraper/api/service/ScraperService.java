package com.sports.scraper.api.service;

import java.util.Set;

import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

public interface ScraperService {

    Set<PlayerPerGameStatsDto> getPlayerPerGameForSeasonByTeam(int year, int pageSize);

    Set<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year);

    Set<TeamPerGameDto> getTeamPerGameStats(int year);
}
