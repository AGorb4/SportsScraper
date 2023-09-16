package com.sports.scraper.api.service.scraper.players;

import java.util.List;

import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;

public interface PlayerScraperService {

        List<PlayerPerGameStatsDto> getPlayersPerGameForSeason(int year, int pageSize);

        PlayerPerGameStatsDto getPlayerPerGameForSeason(String playerName, int year);

        List<PlayerGameLogDto> getPlayerGameLogForYear(String player, int year,
                        boolean sortCron);

        List<PlayerAdvancedGameLogDto> getPlayerAdvancedGameLogForYear(String player,
                        int year);

        List<PlayerGameLogDto> getPlayerGameLogVsTeam(String player, String team, int year);
}
