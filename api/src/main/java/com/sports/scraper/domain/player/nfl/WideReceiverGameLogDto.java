package com.sports.scraper.domain.player.nfl;

import lombok.Data;

@Data
public class WideReceiverGameLogDto extends NFLPlayerGameLogDto {
    public WideReceiverGameLogDto() {
        super();
    }

    private int targets;
    private int receptions;
    private int yards;
    private String yardsPerReception;
    private int receivingTouchdowns;

    private String catchPercentage;
    private String yardsPerTarget;
}
