package com.sports.scraper.api.service;

import java.util.List;

import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

import org.jsoup.nodes.Document;

public interface ScraperService {

    List<PlayerPerGameStatsDto> getPlayerPerGameForSeason(int year, int pageSize);

    List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year);

    List<PlayerGameLogDto> getPlayerGameLogVsTeam(String player, String team, int year);

    List<TeamPerGameDto> getTeamPerGameStats(int year);

    Document getDocumentForURL(String url) throws ScrapingException;
}
