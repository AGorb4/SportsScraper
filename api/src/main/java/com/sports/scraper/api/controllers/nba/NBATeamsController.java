package com.sports.scraper.api.controllers.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.sports.scraper.api.service.scraper.players.NBAPlayerScraperServiceImpl;
import com.sports.scraper.domain.teams.nba.NBATeamPerGame;

@RestController
@RequestMapping(path = "/nba/teams")
public class NBATeamsController {

    @Autowired
    @Qualifier("nbaPlayerScraperServiceImpl")
    NBAPlayerScraperServiceImpl scraperService;

    @GetMapping(path = "/")
    public ResponseEntity<List<NBATeamPerGame>> getNBATeams(@PathVariable int year) {
        List<NBATeamPerGame> teamsPerGameList = scraperService.getTeamPerGameStats(year);
        return new ResponseEntity<>(teamsPerGameList, HttpStatus.OK);
    }

    @GetMapping(path = "/{year}")
    public ResponseEntity<List<NBATeamPerGame>> getTeamPerGameStats(@PathVariable int year) {
        List<NBATeamPerGame> teamsPerGameList = scraperService.getTeamPerGameStats(year);
        return new ResponseEntity<>(teamsPerGameList, HttpStatus.OK);
    }
}
