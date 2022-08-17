package com.sports.scraper.api.service.scraper.nfl;

import java.util.List;

import org.jsoup.nodes.Document;

import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.api.service.scraper.ScraperService;
import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

public class NFLScraperServiceImpl implements ScraperService {

    @Override
    public List<PlayerPerGameStatsDto> getPlayersPerGameForSeason(int year, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PlayerPerGameStatsDto getPlayerPerGameForSeason(String playerName, int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year, boolean sortCron) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PlayerAdvancedGameLogDto> getPlayerAdvancedGameLogForYear(String player, int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PlayerGameLogDto> getPlayerGameLogVsTeam(String player, String team, int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TeamPerGameDto> getTeamPerGameStats(int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPlayerPictureUrl(String playerName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document getDocumentForURL(String url) throws ScrapingException {
        // TODO Auto-generated method stub
        return null;
    }

}
