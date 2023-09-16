package com.sports.scraper.api.controllers.nba;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports.scraper.api.service.scraper.players.PlayerScraperService;
import com.sports.scraper.domain.player.PlayerAdvancedGameLogDto;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;

@RestController
@RequestMapping(path = "/nba/players")
public class NBAPlayersController {

    @Autowired
    @Qualifier("nbaPlayerScraperServiceImpl")
    PlayerScraperService playerScraperService;

    @GetMapping(path = "/{year}/pergame/{pageSize}")
    public ResponseEntity<List<PlayerPerGameStatsDto>> getPlayerPerGameForSeasonByTeam(@PathVariable int year,
            @PathVariable int pageSize) {
        List<PlayerPerGameStatsDto> playersPerGameList = playerScraperService.getPlayersPerGameForSeason(year,
                pageSize);
        return new ResponseEntity<>(playersPerGameList, HttpStatus.OK);
    }

    @GetMapping(path = "/{year}/pergame/{playerOneName}/{playerTwoName}")
    public ResponseEntity<List<PlayerPerGameStatsDto>> getPlayerPerGameComparison(@PathVariable int year,
            @PathVariable String playerOneName, @PathVariable String playerTwoName) {

        if (playerOneName.isEmpty() || playerTwoName.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<PlayerPerGameStatsDto> playersPerGameList = playerScraperService.getPlayersPerGameForSeason(year, 0);

        List<PlayerPerGameStatsDto> filteredPlayers = playersPerGameList.stream()
                .filter(p -> p.getSystemName().equals(playerOneName) || p.getSystemName().equals(playerTwoName))
                .collect(Collectors.toList());

        if (filteredPlayers.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(filteredPlayers, HttpStatus.OK);
    }

    @GetMapping(path = "/{year}/gamelog/{player}")
    public ResponseEntity<List<PlayerGameLogDto>> getPlayerGameLogForYear(@PathVariable String player,
            @PathVariable int year) {
        List<PlayerGameLogDto> playersGameLogList = playerScraperService.getPlayerGameLogForYear(player, year,
                false);
        return new ResponseEntity<>(playersGameLogList, HttpStatus.OK);
    }

    @GetMapping(path = "/{year}/gamelog/advanced/{player}")
    public ResponseEntity<List<PlayerAdvancedGameLogDto>> getPlayerAdvancedGameLogForYear(@PathVariable String player,
            @PathVariable int year) {
        List<PlayerAdvancedGameLogDto> playersGameLogList = playerScraperService.getPlayerAdvancedGameLogForYear(player,
                year);
        return new ResponseEntity<>(playersGameLogList, HttpStatus.OK);
    }

    @GetMapping(path = "/{year}/gamelog/{player}/against/{vsTeam}")
    public ResponseEntity<List<PlayerGameLogDto>> getPlayerGameLogVsTeam(@PathVariable String player,
            @PathVariable int year, @PathVariable String vsTeam) {
        List<PlayerGameLogDto> playersGameLogList = playerScraperService.getPlayerGameLogVsTeam(player, vsTeam, year);
        return new ResponseEntity<>(playersGameLogList, HttpStatus.OK);
    }
}
