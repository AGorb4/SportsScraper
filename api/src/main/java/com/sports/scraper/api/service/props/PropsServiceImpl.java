package com.sports.scraper.api.service.props;

import java.util.ArrayList;
import java.util.List;

import com.sports.scraper.api.constants.PropTypeConstants;
import com.sports.scraper.api.service.scraper.ScraperService;
import com.sports.scraper.api.service.scraper.players.NFLPlayerScraperServiceImpl;
import com.sports.scraper.api.service.scraper.players.PlayerScraperService;
import com.sports.scraper.api.utils.URLUtils;
import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.PlayerPerGameStatsDto;
import com.sports.scraper.domain.player.nba.NBAPlayerGameLogDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsDto;
import com.sports.scraper.domain.player.nfl.gamelog.NFLPlayerGameLogDto;
import com.sports.scraper.domain.props.draftkings.responses.DraftkingsResponse;
import com.sports.scraper.domain.props.draftkings.responses.Event;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsDto;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsReportRequest;
import com.sports.scraper.domain.props.statistics.responses.PropStatisticsReportRequest;
import com.sports.scraper.domain.props.statistics.responses.PropStatisticsReportResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class PropsServiceImpl implements PropsService {

    private String draftkingsUrl = "https://sportsbook.draftkings.com//sites/US-SB/api/v4/eventgroups/88670846";

    @Autowired
    private ScraperService scraperService;

    @Autowired
    @Qualifier("nbaPlayerScraperServiceImpl")
    private PlayerScraperService nbaPlayerScraperService;

    @Autowired
    @Qualifier("nflPlayerScraperServiceImpl")
    private NFLPlayerScraperServiceImpl nflPlayerScraperService;

    @Override
    public List<OfferCategory> getPropTypes() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<DraftkingsResponse> response = restTemplate.getForEntity(draftkingsUrl,
                DraftkingsResponse.class);
        if (response.getStatusCode().value() == 200 && response.hasBody()) {
            log.info("Success getting draftkings offer categories");
            DraftkingsResponse draftkingsResponse = response.getBody();
            if (draftkingsResponse != null) {
                if (draftkingsResponse.getEventGroup() != null) {
                    for (OfferCategory category : draftkingsResponse.getEventGroup().getOfferCategories()) {
                        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty())
                            category.setSubCategories(new ArrayList<>());
                    }
                    return draftkingsResponse.getEventGroup().getOfferCategories();
                } else {
                    log.error("Draftkings event group object is null");
                }
            } else {
                log.error("Null response body getting draftkings offer categories");
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<Event> getEvents(String league) {
        RestTemplate restTemplate = new RestTemplate();
        league = league.toUpperCase();
        String url = URLUtils.getDraftkingsUrlMap().get(league);
        ResponseEntity<DraftkingsResponse> response = restTemplate.getForEntity(url,
                DraftkingsResponse.class);
        if (response.getStatusCode().value() == 200 && response.hasBody()) {
            log.info("Success getting draftkings events for the " + league);
            DraftkingsResponse draftkingsResponse = response.getBody();
            if (draftkingsResponse != null) {
                if (draftkingsResponse.getEventGroup() != null) {
                    return draftkingsResponse.getEventGroup().getEvents();
                } else {
                    log.error("Draftkings event group object is null");
                }
            } else {
                log.error("Null response body getting draftkings offer categories");
            }
        }
        return new ArrayList<>();
    }

    @Override
    public PlayerPropStatisticsDto getPlayerPropStatistics(String league, String playerName, String propType,
            float propTotal,
            int lastNInput,
            String againstTeam,
            int year, boolean includeGamelog) {
        if (league.contentEquals("NBA")) {
            return getNbaPlayerPropStatistics(playerName, propType, propTotal, lastNInput, year,
                    includeGamelog);
        } else {
            return getNflPlayerPropStatistics(playerName, propType, propTotal, lastNInput, year,
                    includeGamelog);
        }
    }

    public PlayerPropStatisticsDto getNbaPlayerPropStatistics(String playerName, String propType, float propTotal,
            int lastNInput,
            int year, boolean includeGamelog) {
        PlayerPerGameStatsDto playerPerGameStatsDto = nbaPlayerScraperService.getPlayerPerGameForSeason(playerName,
                year);
        log.info("Got player per game for season");
        PlayerPropStatisticsDto playerPropStatisticsDto = new PlayerPropStatisticsDto();
        playerPropStatisticsDto
                .setPlayerPictureUrl(scraperService.getPlayerPictureUrl("NBA", playerPerGameStatsDto.getSystemName()));
        log.info("Got player picture");
        playerPropStatisticsDto.setPropType(propType);
        playerPropStatisticsDto.setPropTotal(propTotal);
        playerPropStatisticsDto.setPlayerAverage(getNBAPlayerAverageForStat(playerPerGameStatsDto, propType));
        playerPropStatisticsDto.setGamesPlayed(playerPerGameStatsDto.getGamesCount());
        playerPropStatisticsDto.setGamesStarted(playerPerGameStatsDto.getGamesStartedCount());

        List<PlayerGameLogDto> playerGameLogList = nbaPlayerScraperService
                .getPlayerGameLogForYear(playerPerGameStatsDto.getSystemName(), year, true);
        log.info("Got player game log for year");
        log.info("Prop type : " + propType);
        List<PlayerGameLogDto> responseGameLogsList = new ArrayList<>();

        if (lastNInput > 0) {
            if (playerGameLogList.size() < lastNInput) {
                // todo throw exception
            }

            int hitCounter = 0;
            int gamesCounter = lastNInput;
            int gamesPlayed = 0;
            for (int i = 0; i < gamesCounter && i < playerGameLogList.size(); i++) {
                NBAPlayerGameLogDto gameLog = (NBAPlayerGameLogDto) playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0) {
                    gamesPlayed++;
                    if (Float.compare(getStatFromGameLogNba(gameLog, propType), propTotal) != -1)
                        hitCounter++;
                } else {
                    gamesCounter++;
                }
            }
            playerPropStatisticsDto.setLastNRecord(hitCounter + "-" + (gamesPlayed - hitCounter));
            playerPropStatisticsDto.setLastNWinPercentage((hitCounter * 100) / gamesPlayed);
        } else {
            int hitCounter = 0;
            for (int i = 0; i < playerGameLogList.size(); i++) {
                NBAPlayerGameLogDto gameLog = (NBAPlayerGameLogDto) playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0
                        && Float.compare(getStatFromGameLogNba(gameLog, propType), propTotal) != -1) {
                    hitCounter++;
                }
            }
            playerPropStatisticsDto
                    .setLastNRecord(hitCounter + "-" + (playerPerGameStatsDto.getGamesCount() - hitCounter));
            playerPropStatisticsDto.setLastNWinPercentage((hitCounter * 100) / playerPerGameStatsDto.getGamesCount());
        }

        if (includeGamelog)
            playerPropStatisticsDto.setGameLog(responseGameLogsList);

        return playerPropStatisticsDto;
    }

    // TODO finish prop statistics for NFL
    public PlayerPropStatisticsDto getNflPlayerPropStatistics(String playerName, String propType, float propTotal,
            int lastNInput,
            int year, boolean includeGamelog) {
        NFLPlayerFantasyStatsDto nflPlayerFantasyStatsDto = nflPlayerScraperService.getNFLPlayerFantasyStats(playerName,
                year);
        log.info("Got player per game for season");
        PlayerPropStatisticsDto playerPropStatisticsDto = new PlayerPropStatisticsDto();
        playerPropStatisticsDto
                .setPlayerPictureUrl(
                        scraperService.getPlayerPictureUrl("NFL", nflPlayerFantasyStatsDto.getPlayerSystemName()));
        log.info("Got player picture");
        playerPropStatisticsDto.setPropType(propType);
        playerPropStatisticsDto.setPropTotal(propTotal);
        playerPropStatisticsDto.setPlayerAverage(getNFLPlayerAverageForStat(nflPlayerFantasyStatsDto, propType));
        playerPropStatisticsDto.setGamesPlayed(nflPlayerFantasyStatsDto.getGamesPlayed());
        playerPropStatisticsDto.setGamesStarted(nflPlayerFantasyStatsDto.getGamesStarted());

        List<PlayerGameLogDto> playerGameLogList = nflPlayerScraperService
                .getPlayerGameLogForYear(nflPlayerFantasyStatsDto.getPlayerName(), year, true);
        log.info("Got player game log for year");
        List<PlayerGameLogDto> responseGameLogsList = new ArrayList<>();

        if (lastNInput > 0) {
            if (playerGameLogList.size() < lastNInput) {
                // todo throw exception
            }

            int hitCounter = 0;
            int gamesCounter = lastNInput;
            int gamesPlayed = playerPropStatisticsDto.getGamesPlayed();
            for (int i = 0; i < gamesCounter && i < playerGameLogList.size(); i++) {
                NFLPlayerGameLogDto gameLog = (NFLPlayerGameLogDto) playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0) {
                    if (Float.compare(getStatFromGameLogNfl(gameLog, propType), propTotal) != -1)
                        hitCounter++;
                } else {
                    gamesCounter++;
                }
            }
            playerPropStatisticsDto
                    .setLastNRecord(hitCounter + "-" + (nflPlayerFantasyStatsDto.getGamesPlayed() - hitCounter));
            playerPropStatisticsDto.setLastNWinPercentage((hitCounter * 100) / gamesPlayed);
        } else {
            int hitCounter = 0;
            for (int i = 0; i < playerGameLogList.size(); i++) {
                NFLPlayerGameLogDto gameLog = (NFLPlayerGameLogDto) playerGameLogList.get(i);
                responseGameLogsList.add(gameLog);
                if (gameLog.getGameCount() != 0
                        && Float.compare(getStatFromGameLogNfl(gameLog, propType), propTotal) != -1) {
                    hitCounter++;
                }
            }
            playerPropStatisticsDto
                    .setLastNRecord(hitCounter + "-" + (nflPlayerFantasyStatsDto.getGamesPlayed() - hitCounter));
            playerPropStatisticsDto
                    .setLastNWinPercentage((hitCounter * 100) / nflPlayerFantasyStatsDto.getGamesPlayed());
        }

        if (includeGamelog)
            playerPropStatisticsDto.setGameLog(responseGameLogsList);

        return playerPropStatisticsDto;
    }

    private float getNBAPlayerAverageForStat(PlayerPerGameStatsDto playerPerGameStatsDto, String propType) {
        if (propType.equalsIgnoreCase(PropTypeConstants.POINTS)) {
            return playerPerGameStatsDto.getPoints();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.THREES)) {
            return playerPerGameStatsDto.getThreePointers();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.ASSISTS)) {
            return playerPerGameStatsDto.getAssists();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.REBOUNDS)) {
            return playerPerGameStatsDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.STEALS)) {
            return playerPerGameStatsDto.getSteals();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.BLOCKS)) {
            return playerPerGameStatsDto.getBlocks();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.TURNOVERS)) {
            return playerPerGameStatsDto.getTurnovers();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.POINTS_REBOUNDS_ASSISTS)) {
            return playerPerGameStatsDto.getPoints() + playerPerGameStatsDto.getTotalRebounds()
                    + playerPerGameStatsDto.getAssists();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.POINTS_REBOUNDS)) {
            return playerPerGameStatsDto.getPoints() + playerPerGameStatsDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.POINTS_ASSISTS)) {
            return playerPerGameStatsDto.getPoints() + playerPerGameStatsDto.getAssists();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.ASSISTS_REBOUNDS)) {
            return playerPerGameStatsDto.getAssists() + playerPerGameStatsDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.STEALS_BLOCKS)) {
            return playerPerGameStatsDto.getSteals() + playerPerGameStatsDto.getBlocks();
        } else {
            // todo throw an exception
            return 0;
        }
    }

    private float getNFLPlayerAverageForStat(NFLPlayerFantasyStatsDto nflPlayerFantasyStatsDto, String propType) {
        if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_YARDS)) {
            return nflPlayerFantasyStatsDto.getPassingYards() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RECEIVING_YARDS)) {
            return nflPlayerFantasyStatsDto.getReceivingYards() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RUSHING_YARDS)) {
            return nflPlayerFantasyStatsDto.getRushingYards() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_COMPLETIONS)) {
            return nflPlayerFantasyStatsDto.getPassesCompleted() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_ATTEMPTS)) {
            return nflPlayerFantasyStatsDto.getPassesAttempted() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_TOUCHDOWNS)) {
            return nflPlayerFantasyStatsDto.getPassingTouchdowns() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.CARRIES)) {
            return nflPlayerFantasyStatsDto.getRushingAttempts() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RUSHING_TOUCHDOWNS)) {
            return nflPlayerFantasyStatsDto.getRushingTouchdowns() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RECEIVING_TOUCHDOWNS)) {
            return nflPlayerFantasyStatsDto.getReceivingTouchdowns() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RECEPTIONS)) {
            return nflPlayerFantasyStatsDto.getReceptions() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.TOUCHDOWNS)) {
            return nflPlayerFantasyStatsDto.getTouchdowns() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.TARGETS)) {
            return nflPlayerFantasyStatsDto.getTargets() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.INTERCEPTIONS)) {
            return nflPlayerFantasyStatsDto.getInterceptions() / nflPlayerFantasyStatsDto.getGamesPlayed();
        } else {
            // todo throw an exception
            return 0;
        }
    }

    private int getStatFromGameLogNba(NBAPlayerGameLogDto playerGameLogDto, String propType) {
        if (propType.equalsIgnoreCase(PropTypeConstants.POINTS)) {
            return playerGameLogDto.getPoints();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.THREES)) {
            return playerGameLogDto.getThreePointers();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.ASSISTS)) {
            return playerGameLogDto.getAssists();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.REBOUNDS)) {
            return playerGameLogDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.STEALS)) {
            return playerGameLogDto.getSteals();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.BLOCKS)) {
            return playerGameLogDto.getBlocks();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.TURNOVERS)) {
            return playerGameLogDto.getTurnovers();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.POINTS_REBOUNDS_ASSISTS)) {
            return playerGameLogDto.getPoints() + playerGameLogDto.getTotalRebounds()
                    + playerGameLogDto.getAssists();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.POINTS_REBOUNDS)) {
            return playerGameLogDto.getPoints() + playerGameLogDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.POINTS_ASSISTS)) {
            return playerGameLogDto.getPoints() + playerGameLogDto.getAssists();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.ASSISTS_REBOUNDS)) {
            return playerGameLogDto.getAssists() + playerGameLogDto.getTotalRebounds();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.STEALS_BLOCKS)) {
            return playerGameLogDto.getSteals() + playerGameLogDto.getBlocks();
        } else {
            // todo throw an exception
            return 0;
        }
    }

    private int getStatFromGameLogNfl(NFLPlayerGameLogDto playerGameLogDto, String propType) {
        if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_YARDS) && playerGameLogDto.getPassingStats() != null) {
            return playerGameLogDto.getPassingStats().getPassingYards();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RECEIVING_YARDS)
                && playerGameLogDto.getReceivingStats() != null) {
            return playerGameLogDto.getReceivingStats().getReceivingYards();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RUSHING_YARDS)
                && playerGameLogDto.getRushingStats() != null) {
            return playerGameLogDto.getRushingStats().getRushingYards();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_COMPLETIONS)
                && playerGameLogDto.getPassingStats() != null) {
            return playerGameLogDto.getPassingStats().getPassesCompleted();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_ATTEMPTS)
                && playerGameLogDto.getPassingStats() != null) {
            return playerGameLogDto.getPassingStats().getPassesAttempted();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.PASSING_TOUCHDOWNS)
                && playerGameLogDto.getPassingStats() != null) {
            return playerGameLogDto.getPassingStats().getPassingTouchdowns();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.CARRIES) && playerGameLogDto.getRushingStats() != null) {
            return playerGameLogDto.getRushingStats().getRushingAttempts();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RUSHING_TOUCHDOWNS)
                && playerGameLogDto.getRushingStats() != null) {
            return playerGameLogDto.getRushingStats().getRushingTouchdowns();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RECEIVING_TOUCHDOWNS)
                && playerGameLogDto.getReceivingStats() != null) {
            return playerGameLogDto.getReceivingStats().getReceivingTouchdowns();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.RECEPTIONS)
                && playerGameLogDto.getReceivingStats() != null) {
            return playerGameLogDto.getReceivingStats().getReceptions();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.TOUCHDOWNS)
                && playerGameLogDto.getScoringStats() != null) {
            return playerGameLogDto.getScoringStats().getTouchdowns();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.TARGETS)
                && playerGameLogDto.getReceivingStats() != null) {
            return playerGameLogDto.getReceivingStats().getTargets();
        } else if (propType.equalsIgnoreCase(PropTypeConstants.INTERCEPTIONS)
                && playerGameLogDto.getPassingStats() != null) {
            return playerGameLogDto.getPassingStats().getInterceptions();
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
        String league = playerPropStatisticsReportRequest.getLeague();
        for (PropStatisticsReportRequest propRequest : playerPropRequestList) {
            List<PlayerPropStatisticsDto> playerPropResponseList = new ArrayList<>();
            PropStatisticsReportResponse playerResponse = new PropStatisticsReportResponse();
            String playerName = propRequest.getPlayerName();
            playerResponse.setPlayerName(playerName);
            if (propRequest.getPointsPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "pts", propRequest.getPointsPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getThreePointersPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "3pt", propRequest.getThreePointersPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getRebsPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "reb", propRequest.getRebsPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getAstsPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "ast", propRequest.getAstsPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getStlsPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "stl", propRequest.getStlsPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getBlksPropTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "blk", propRequest.getBlksPropTotal(),
                                lastN, null, year, false));
            if (propRequest.getTurnoversTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "to", propRequest.getTurnoversTotal(),
                                lastN, null, year, false));
            if (propRequest.getPtsRebAstTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "ptsRebAst", propRequest.getPtsRebAstTotal(),
                                lastN, null, year, false));
            if (propRequest.getPtsRebTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "ptsReb", propRequest.getPtsRebTotal(),
                                lastN, null, year, false));
            if (propRequest.getPtsAstTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "ptsAst", propRequest.getPtsAstTotal(),
                                lastN, null, year, false));
            if (propRequest.getAstRebTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "astsReb", propRequest.getAstRebTotal(),
                                lastN, null, year, false));
            if (propRequest.getStlsBlksTotal() > 0)
                playerPropResponseList
                        .add(getPlayerPropStatistics(league, playerName, "stlsBlk", propRequest.getStlsBlksTotal(),
                                lastN, null, year, false));

            playerResponse.setPlayerPropStatisticsList(playerPropResponseList);
            responseList.add(playerResponse);
        }

        return responseList;
    }
}
