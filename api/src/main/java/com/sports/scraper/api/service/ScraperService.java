package com.sports.scraper.api.service;

import java.util.List;

import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

import org.jsoup.nodes.Document;

public interface ScraperService {

    List<PlayerPerGameStatsDto> getPlayersPerGameForSeason(int year, int pageSize);

    PlayerPerGameStatsDto getPlayerPerGameForSeason(String playerName, int year);

    List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year, boolean sortCron);

    List<PlayerAdvancedGameLogDto> getPlayerAdvancedGameLogForYear(String player, int year);

    List<PlayerGameLogDto> getPlayerGameLogVsTeam(String player, String team, int year);

    List<TeamPerGameDto> getTeamPerGameStats(int year);

    String getPlayerPictureUrl(String playerName);

    Document getDocumentForURL(String url) throws ScrapingException;
}
