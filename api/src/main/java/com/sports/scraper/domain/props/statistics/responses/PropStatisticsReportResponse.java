package com.sports.scraper.domain.props.statistics.responses;

import java.util.List;

import lombok.Data;

@Data
public class PropStatisticsReportResponse {
    private String playerName;
    private List<PlayerPropStatisticsDto> playerPropStatisticsList;
}
