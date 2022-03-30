package com.sports.scraper.api.controllers;

import java.util.List;

import com.sports.scraper.api.service.ScraperService;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/players")
public class PlayersController {

    @Autowired
    ScraperService scraperService;

    @GetMapping(path = "/{year}/pergame/{pageSize}")
    public ResponseEntity<List<PlayerPerGameStatsDto>> getPlayerPerGameForSeasonByTeam(@PathVariable int year,
            @PathVariable int pageSize) {
        List<PlayerPerGameStatsDto> playersPerGameList = scraperService.getPlayerPerGameForSeasonByTeam(year, pageSize);
        return new ResponseEntity<>(playersPerGameList, HttpStatus.OK);
    }

    @GetMapping(path = "/{year}/gamelog/{player}")
    public ResponseEntity<List<PlayerGameLogDto>> getPlayerGameLogForYear(@PathVariable String player,
            @PathVariable int year) {
        List<PlayerGameLogDto> playersGameLogList = scraperService.getPlayerGameLogForYear(player, year);
        return new ResponseEntity<>(playersGameLogList, HttpStatus.OK);
    }
}
