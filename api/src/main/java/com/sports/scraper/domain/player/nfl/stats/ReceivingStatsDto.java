package com.sports.scraper.domain.player.nfl.stats;

import lombok.Data;

@Data
public class ReceivingStatsDto {
    private int targets;
    private int receptions;
    private int receivingYards;
    private String yardsPerReception;
    private int receivingTouchdowns;

    private String catchPercentage;
    private String yardsPerTarget;
}
