package com.sports.scraper.api.controllers.nfl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports.scraper.api.service.scraper.nfl.NFLScraperServiceImpl;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerNameDto;
import com.sports.scraper.domain.player.nfl.NFLPlayerProfileDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsDto;

@RestController
@RequestMapping(path = "/nfl/players")
public class NFLPlayersController {

    @Autowired
    NFLScraperServiceImpl scraperService;

    @GetMapping(path = "/{year}")
    public ResponseEntity<List<PlayerNameDto>> getPlayersByYear(@PathVariable int year) {
        List<PlayerNameDto> playersPerGameList = scraperService.getAllPlayerNames(year);
        return new ResponseEntity<>(playersPerGameList, HttpStatus.OK);
    }

    @GetMapping(path = "/years/{year}/fantasy")
    public ResponseEntity<List<NFLPlayerFantasyStatsDto>> getPlayerPerGameForSeasonByTeam(@PathVariable int year) {
        List<NFLPlayerFantasyStatsDto> playersPerGameList = scraperService.getAllNFLPlayersFantasyStats(year);
        return new ResponseEntity<>(playersPerGameList, HttpStatus.OK);
    }

    @GetMapping(path = "/years/{year}/player/{playerName}/gamelog")
    public ResponseEntity<List<PlayerGameLogDto>> getPlayerGameLogForYear(@PathVariable int year,
            @PathVariable String playerName) {
        List<PlayerGameLogDto> playersPerGameList = scraperService.getPlayerGameLogForYear(playerName, year,
                true);
        return new ResponseEntity<>(playersPerGameList, HttpStatus.OK);
    }

    @GetMapping(path = "/{year}/player/{playerName}/profile")
    public ResponseEntity<NFLPlayerProfileDto> getPlayerProfileForYear(@PathVariable int year,
            @PathVariable String playerName) {
        NFLPlayerProfileDto playersPerGameList = scraperService.getNflPlayerProfile(playerName, year);
        return new ResponseEntity<>(playersPerGameList, HttpStatus.OK);
    }
}
