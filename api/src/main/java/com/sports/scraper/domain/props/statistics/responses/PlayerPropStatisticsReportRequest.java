package com.sports.scraper.domain.props.statistics.responses;

import java.util.List;

import lombok.Data;

@Data
public class PlayerPropStatisticsReportRequest {
    private int year;
    private int lastN;
    private List<PropStatisticsReportRequest> propStatisticsReportRequestList;
}