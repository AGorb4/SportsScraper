package com.sports.scraper.domain.player.nfl.stats;

import lombok.Data;

@Data
public class KickReturnStatsDto {
    private int returns;
    private int yards;
    private String yardsPerReturn;
    private int returnTouchdowns;
}
