package com.sports.scraper.api.service.teams;

import java.util.List;

import com.sports.scraper.domain.teams.Team;

public interface TeamsService {
    List<Team> getTeamsByLeague(String league);
}
