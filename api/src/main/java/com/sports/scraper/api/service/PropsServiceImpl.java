package com.sports.scraper.api.service;

import java.util.ArrayList;
import java.util.List;

import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.props.draftkings.responses.DraftkingsResponse;
import com.sports.scraper.domain.props.draftkings.responses.Event;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PropsServiceImpl implements PropsService {

    private String draftkingsUrl = "https://sportsbook.draftkings.com//sites/US-SB/api/v4/eventgroups/88670846";

    @Autowired
    private ScraperService scraperService;

    @Override
    public List<OfferCategory> getPropTypes() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<DraftkingsResponse> response = restTemplate.getForEntity(draftkingsUrl,
                DraftkingsResponse.class);
        if (response.getStatusCode().value() == 200 && response.hasBody()) {
            System.out.println("Success getting draftkings offer categories");
            DraftkingsResponse draftkingsResponse = response.getBody();
            if (draftkingsResponse != null) {
                if (draftkingsResponse.getEventGroup() != null) {
                    for (OfferCategory category : draftkingsResponse.getEventGroup().getOfferCategories()) {
                        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty())
                            category.setSubCategories(new ArrayList<>());
                    }
                    return draftkingsResponse.getEventGroup().getOfferCategories();
                } else {
                    System.out.println("Draftkings event group object is null");
                }
            } else {
                System.out.println("Null response body getting draftkings offer categories");
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<Event> getEvents() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<DraftkingsResponse> response = restTemplate.getForEntity(draftkingsUrl,
                DraftkingsResponse.class);
        if (response.getStatusCode().value() == 200 && response.hasBody()) {
            System.out.println("Success getting draftkings events");
            DraftkingsResponse draftkingsResponse = response.getBody();
            if (draftkingsResponse != null) {
                if (draftkingsResponse.getEventGroup() != null) {
                    return draftkingsResponse.getEventGroup().getEvents();
                } else {
                    System.out.println("Draftkings event group object is null");
                }
            } else {
                System.out.println("Null response body getting draftkings offer categories");
            }
        }
        return new ArrayList<>();
    }

    @Override
    public PlayerPropStatisticsDto getPlayerPropStatistics(String playerName, String propType, float propTotal,
            int lastNInput,
            String againstTeam,
            int year) {

        PlayerPerGameStatsDto playerPerGameStatsDto = scraperService.getPlayerPerGameForSeason(playerName, year);

        PlayerPropStatisticsDto playerPropStatisticsDto = new PlayerPropStatisticsDto();
        playerPropStatisticsDto.setPropType(propType);
        playerPropStatisticsDto.setPropTotal(propTotal);
        playerPropStatisticsDto.setPlayerAverage(getPlayerAverageForStat(playerPerGameStatsDto, propType));
        playerPropStatisticsDto.setLastNInput(lastNInput);
        playerPropStatisticsDto.setGamesPlayed(playerPerGameStatsDto.getGamesCount());
        playerPropStatisticsDto.setGamesStarted(playerPerGameStatsDto.getGamesStartedCount());

        List<PlayerGameLogDto> playerGameLogList = scraperService.getPlayerGameLogForYear(playerName, year, true);
        List<PlayerGameLogDto> responseGameLogsList = new ArrayList<>();

        if (lastNInput > 0) {
            if (playerGameLogList.size() < lastNInput) {
                // todo throw exception
            }

            int hitCounter = 0;
            int gamesCounter = lastNInput;
            for (int i = 0; i < gamesCounter; i++) {
                PlayerGameLogDto gameLog = playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0) {
                    if (Float.compare(getStatFromGameLog(gameLog, propType), propTotal) != -1)
                        hitCounter++;
                } else {
                    gamesCounter++;
                }
            }
            playerPropStatisticsDto.setLastNRecord(hitCounter + "-" + lastNInput);
            playerPropStatisticsDto.setLastNWinPercentage((hitCounter * 100) / lastNInput);
        } else {
            int hitCounter = 0;
            int gamesCounter = 0;
            for (int i = 0; i < playerGameLogList.size(); i++) {
                PlayerGameLogDto gameLog = playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0) {
                    gamesCounter++;
                    if (Float.compare(getStatFromGameLog(gameLog, propType), propTotal) != -1)
                        hitCounter++;
                }
            }
            playerPropStatisticsDto.setLastNRecord(hitCounter + "-" + gamesCounter);
            playerPropStatisticsDto.setLastNWinPercentage((hitCounter * 100) / gamesCounter);
        }

        playerPropStatisticsDto.setGameLog(responseGameLogsList);
        return playerPropStatisticsDto;
    }

    private float getPlayerAverageForStat(PlayerPerGameStatsDto playerPerGameStatsDto, String propType) {
        if (propType.equalsIgnoreCase("pts")) {
            return playerPerGameStatsDto.getPoints();
        } else if (propType.equalsIgnoreCase("3pt")) {
            return playerPerGameStatsDto.getThreePointers();
        } else if (propType.equalsIgnoreCase("ast")) {
            return playerPerGameStatsDto.getAssists();
        } else if (propType.equalsIgnoreCase("reb")) {
            return playerPerGameStatsDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase("stl")) {
            return playerPerGameStatsDto.getSteals();
        } else if (propType.equalsIgnoreCase("blk")) {
            return playerPerGameStatsDto.getBlocks();
        } else {
            // todo throw an exception
            return 0;
        }
    }

    private int getStatFromGameLog(PlayerGameLogDto playerGameLogDto, String propType) {
        if (propType.equalsIgnoreCase("pts")) {
            return playerGameLogDto.getPoints();
        } else if (propType.equalsIgnoreCase("3pt")) {
            return playerGameLogDto.getThreePointers();
        } else if (propType.equalsIgnoreCase("ast")) {
            return playerGameLogDto.getAssists();
        } else if (propType.equalsIgnoreCase("reb")) {
            return playerGameLogDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase("stl")) {
            return playerGameLogDto.getSteals();
        } else if (propType.equalsIgnoreCase("blk")) {
            return playerGameLogDto.getBlocks();
        } else {
            // todo throw an exception
            return 0;
        }
    }
}
