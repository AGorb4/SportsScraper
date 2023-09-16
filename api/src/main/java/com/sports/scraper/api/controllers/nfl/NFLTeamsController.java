package com.sports.scraper.api.controllers.nfl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.api.service.teams.nfl.NFLTeamsScraperService;
import com.sports.scraper.domain.teams.nfl.NFLTeam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/nfl/teams")
public class NFLTeamsController {

    private NFLTeamsScraperService nflTeamsScraperService;

    @Autowired
    public NFLTeamsController(NFLTeamsScraperService nflTeamsScraperService) {
        this.nflTeamsScraperService = nflTeamsScraperService;
    }

    @GetMapping(path = "/{year}")
    public ResponseEntity<List<NFLTeam>> getNFLTeamsByYear(@PathVariable int year) {
        log.info("Received request for getNFLTeamsByYear " + year);
        List<NFLTeam> nflTeamsList;
        try {
            nflTeamsList = nflTeamsScraperService.getNFLTeams(year);
            return new ResponseEntity<>(nflTeamsList, HttpStatus.OK);
        } catch (ScrapingException e) {
            log.error("Error occured during getNFLTeamsByYear", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
