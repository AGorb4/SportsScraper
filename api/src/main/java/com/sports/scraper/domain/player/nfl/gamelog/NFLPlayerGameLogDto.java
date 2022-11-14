package com.sports.scraper.domain.player.nfl.gamelog;

import com.sports.scraper.domain.player.PlayerGameLogDto;
import com.sports.scraper.domain.player.nfl.stats.FumbleStatsDto;
import com.sports.scraper.domain.player.nfl.stats.KickReturnStatsDto;
import com.sports.scraper.domain.player.nfl.stats.PassingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.ReceivingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.RushingStatsDto;
import com.sports.scraper.domain.player.nfl.stats.ScoringStatsDto;
import com.sports.scraper.domain.player.nfl.stats.SnapStatsDto;
import com.sports.scraper.domain.player.nfl.stats.TacklingStatsDto;

import lombok.Data;

@Data
public class NFLPlayerGameLogDto extends PlayerGameLogDto {

    public NFLPlayerGameLogDto() {
        super();
    }

    private int week;

    private PassingStatsDto passingStats;
    private RushingStatsDto rushingStats;
    private ReceivingStatsDto receivingStats;
    private ScoringStatsDto scoringStats;
    private FumbleStatsDto fumbleStats;
    private KickReturnStatsDto kickReturnStats;
    private KickReturnStatsDto puntReturnStats;
    private TacklingStatsDto tacklingStats;
    private SnapStatsDto offSnapStats;
    private SnapStatsDto defSnapStats;
    private SnapStatsDto stSnapStats;

}
