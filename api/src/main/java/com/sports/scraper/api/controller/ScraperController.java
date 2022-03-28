package com.sports.scraper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

import com.sports.scraper.api.service.ScraperService;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.team.TeamPerGameDto;

@RestController
@RequestMapping(path = "/")
public class ScraperController {

    @Autowired
    ScraperService scraperService;

    @GetMapping(path = "/{year}/{pageSize}")
    public Set<PlayerPerGameStatsDto> getPlayerPerGameForSeasonByTeam(@PathVariable int year,
            @PathVariable int pageSize) {
        return scraperService.getPlayerPerGameForSeasonByTeam(year, pageSize);
    }

    @GetMapping(path = "/gamelog/{player}/{year}")
    public Set<PlayerGameLogDto> getPlayerGameLogForYear(@PathVariable String player, @PathVariable int year) {
        return scraperService.getPlayerGameLogForYear(player, year);
    }

    @GetMapping(path = "/teams/{year}")
    public Set<TeamPerGameDto> getTeamPerGameStats(@PathVariable int year) {
        return scraperService.getTeamPerGameStats(year);
    }
}
