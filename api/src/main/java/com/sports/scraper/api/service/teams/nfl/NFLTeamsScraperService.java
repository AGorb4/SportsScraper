package com.sports.scraper.api.service.teams.nfl;

import java.util.List;

import com.sports.scraper.api.exceptions.ScrapingException;
import com.sports.scraper.domain.teams.nfl.NFLTeam;

public interface NFLTeamsScraperService {
    List<NFLTeam> getNFLTeams(int year) throws ScrapingException;
}
