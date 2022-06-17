package com.sports.scraper.domain.props.statistics.responses;

import lombok.Data;

@Data
public class PropStatisticsReportRequest {
    private String playerName;
    private float pointsPropTotal;
    private float rebsPropTotal;
    private float astsPropTotal;
    private float stlsPropTotal;
    private float blksPropTotal;
    private float threePointersPropTotal;
    private float turnoversTotal;
    private float ptsRebAstTotal;
    private float ptsRebTotal;
    private float ptsAstTotal;
    private float astRebTotal;
    private float stlsBlksTotal;
}
