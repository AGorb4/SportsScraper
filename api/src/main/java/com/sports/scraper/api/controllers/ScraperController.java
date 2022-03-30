package com.sports.scraper.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.sports.scraper.api.service.ScraperService;
import com.sports.scraper.domain.team.TeamPerGameDto;

@RestController
@RequestMapping(path = "/")
public class ScraperController {

    @Autowired
    ScraperService scraperService;

    @GetMapping(path = "/teams/{year}")
    public ResponseEntity<List<TeamPerGameDto>> getTeamPerGameStats(@PathVariable int year) {
        List<TeamPerGameDto> teamsPerGameList = scraperService.getTeamPerGameStats(year);
        return new ResponseEntity<>(teamsPerGameList, HttpStatus.OK);
    }
}
