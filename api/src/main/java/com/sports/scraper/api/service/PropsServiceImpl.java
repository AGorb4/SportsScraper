package com.sports.scraper.api.service;

import java.util.ArrayList;
import java.util.List;

import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.props.draftkings.responses.DraftkingsResponse;
import com.sports.scraper.domain.props.draftkings.responses.Event;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsDto;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsReportRequest;
import com.sports.scraper.domain.props.statistics.responses.PropStatisticsReportRequest;
import com.sports.scraper.domain.props.statistics.responses.PropStatisticsReportResponse;

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
            int year, boolean includeGamelog) {

        PlayerPerGameStatsDto playerPerGameStatsDto = scraperService.getPlayerPerGameForSeason(playerName, year);
        PlayerPropStatisticsDto playerPropStatisticsDto = new PlayerPropStatisticsDto();
        playerPropStatisticsDto
                .setPlayerPictureUrl(scraperService.getPlayerPictureUrl(playerPerGameStatsDto.getSystemName()));
        playerPropStatisticsDto.setPropType(propType);
        playerPropStatisticsDto.setPropTotal(propTotal);
        playerPropStatisticsDto.setPlayerAverage(getPlayerAverageForStat(playerPerGameStatsDto, propType));
        playerPropStatisticsDto.setGamesPlayed(playerPerGameStatsDto.getGamesCount());
        playerPropStatisticsDto.setGamesStarted(playerPerGameStatsDto.getGamesStartedCount());

        List<PlayerGameLogDto> playerGameLogList = scraperService
                .getPlayerGameLogForYear(playerPerGameStatsDto.getSystemName(), year, true);
        List<PlayerGameLogDto> responseGameLogsList = new ArrayList<>();

        if (lastNInput > 0) {
            if (playerGameLogList.size() < lastNInput) {
                // todo throw exception
            }

            int hitCounter = 0;
            int gamesCounter = lastNInput;
            int gamesPlayed = 0;
            for (int i = 0; i < gamesCounter && i < playerGameLogList.size(); i++) {
                PlayerGameLogDto gameLog = playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0) {
                    gamesPlayed++;
                    if (Float.compare(getStatFromGameLog(gameLog, propType), propTotal) != -1)
                        hitCounter++;
                } else {
                    gamesCounter++;
                }
            }
            playerPropStatisticsDto.setLastNRecord(hitCounter + "-" + gamesPlayed);
            playerPropStatisticsDto.setLastNWinPercentage((hitCounter * 100) / gamesPlayed);
        } else {
            int hitCounter = 0;
            for (int i = 0; i < playerGameLogList.size(); i++) {
                PlayerGameLogDto gameLog = playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0) {
                    if (Float.compare(getStatFromGameLog(gameLog, propType), propTotal) != -1)
                        hitCounter++;
                }
            }
            playerPropStatisticsDto.setLastNRecord(hitCounter + "-" + playerPerGameStatsDto.getGamesCount());
            playerPropStatisticsDto.setLastNWinPercentage((hitCounter * 100) / playerPerGameStatsDto.getGamesCount());
        }

        if (includeGamelog)
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
        } else if (propType.equalsIgnoreCase("to")) {
            return playerPerGameStatsDto.getTurnovers();
        } else if (propType.equalsIgnoreCase("ptsRebAst")) {
            return playerPerGameStatsDto.getPoints() + playerPerGameStatsDto.getTotalRebounds()
                    + playerPerGameStatsDto.getAssists();
        } else if (propType.equalsIgnoreCase("ptsReb")) {
            return playerPerGameStatsDto.getPoints() + playerPerGameStatsDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase("ptsAst")) {
            return playerPerGameStatsDto.getPoints() + playerPerGameStatsDto.getAssists();
        } else if (propType.equalsIgnoreCase("astsReb")) {
            return playerPerGameStatsDto.getAssists() + playerPerGameStatsDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase("stlsBlk")) {
            return playerPerGameStatsDto.getSteals() + playerPerGameStatsDto.getBlocks();
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
        } else if (propType.equalsIgnoreCase("to")) {
            return playerGameLogDto.getTurnovers();
        } else if (propType.equalsIgnoreCase("ptsRebAst")) {
            return playerGameLogDto.getPoints() + playerGameLogDto.getTotalRebounds()
                    + playerGameLogDto.getAssists();
        } else if (propType.equalsIgnoreCase("ptsReb")) {
            return playerGameLogDto.getPoints() + playerGameLogDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase("ptsAst")) {
            return playerGameLogDto.getPoints() + playerGameLogDto.getAssists();
        } else if (propType.equalsIgnoreCase("astsReb")) {
            return playerGameLogDto.getAssists() + playerGameLogDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase("stlsBlk")) {
            return playerGameLogDto.getSteals() + playerGameLogDto.getBlocks();
        } else {
            // todo throw an exception
            return 0;
        }
    }

    @Override
    public List<PropStatisticsReportResponse> getPlayerPropStatisticsReport(
            PlayerPropStatisticsReportRequest playerPropStatisticsReportRequest) {
        int lastN = playerPropStatisticsReportRequest.getLastN();
        int year = playerPropStatisticsReportRequest.getYear();
        List<PropStatisticsReportResponse> responseList = new ArrayList<>();
        List<PropStatisticsReportRequest> playerPropRequestList = playerPropStatisticsReportRequest
                .getPropStatisticsReportRequestList();

        for (PropStatisticsReportRequest propRequest : playerPropRequestList) {
            List<PlayerPropStatisticsDto> playerPropResponseList = new ArrayList<>();
            PropStatisticsReportResponse playerResponse = new PropStatisticsReportResponse();
            String playerName = propRequest.getPlayerName();
            playerResponse.setPlayerName(playerName);
            if (propRequest.getPointsPropTotal() > 0)
                playerPropResponseList.add(getPlayerPropStatistics(playerName, "pts", propRequest.getPointsPropTotal(),
                        lastN, null, year, false));
            if (propRequest.getThreePointersPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "3pt", propRequest.getThreePointersPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getRebsPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "reb", propRequest.getRebsPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getAstsPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "ast", propRequest.getAstsPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getStlsPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "stl", propRequest.getStlsPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getBlksPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "blk", propRequest.getBlksPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getTurnoversTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "to", propRequest.getTurnoversTotal(),
                                lastN, null, year, false));
            if (propRequest.getPtsRebAstTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "ptsRebAst", propRequest.getPtsRebAstTotal(),
                                lastN, null, year, false));
            if (propRequest.getPtsRebTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "ptsReb", propRequest.getPtsRebTotal(),
                                lastN, null, year, false));
            if (propRequest.getPtsAstTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "ptsAst", propRequest.getPtsAstTotal(),
                                lastN, null, year, false));
            if (propRequest.getAstRebTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "astsReb", propRequest.getAstRebTotal(),
                                lastN, null, year, false));
            if (propRequest.getStlsBlksTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(playerName, "stlsBlk", propRequest.getStlsBlksTotal(),
                                lastN, null, year, false));

            playerResponse.setPlayerPropStatisticsList(playerPropResponseList);
            responseList.add(playerResponse);
        }

        return responseList;
    }
}
