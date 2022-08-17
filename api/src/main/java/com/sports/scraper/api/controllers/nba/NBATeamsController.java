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

import com.sports.scraper.api.service.scraper.ScraperService;
import com.sports.scraper.domain.team.TeamPerGameDto;

@RestController
@RequestMapping(path = "/nba/teams")
public class NBATeamsController {

    @Autowired
    @Qualifier("nbaScraperServiceImpl")
    ScraperService scraperService;

    @GetMapping(path = "/{year}")
    public ResponseEntity<List<TeamPerGameDto>> getTeamPerGameStats(@PathVariable int year) {
        List<TeamPerGameDto> teamsPerGameList = scraperService.getTeamPerGameStats(year);
        return new ResponseEntity<>(teamsPerGameList, HttpStatus.OK);
    }
}
