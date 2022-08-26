package com.sports.scraper.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;

import com.sports.scraper.domain.player.nfl.gamelog.NFLPlayerGameLogDto;
import com.sports.scraper.domain.player.nfl.gamelog.NFLPlayerGameLogFactory;
import com.sports.scraper.domain.player.nfl.gamelog.WideReceiverGameLogDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsDto;
import com.sports.scraper.domain.player.nfl.fantasy.NFLPlayerFantasyStatsFactory;

public class NFLPlayerMapperUtils {

    private NFLPlayerMapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static NFLPlayerGameLogDto mapNFLPlayerGameLogDto(Elements columns, String position) {
        NFLPlayerGameLogDto nflPlayerGameLogDto = NFLPlayerGameLogFactory.createNFLPlayerGameLog(position);
        nflPlayerGameLogDto.setDate(columns.get(0).text());
        nflPlayerGameLogDto.setGameCount(MapperUtils.getInteger(columns.get(1).text()));
        nflPlayerGameLogDto.setWeek(MapperUtils.getInteger(columns.get(2).text()));
        nflPlayerGameLogDto.setAge(columns.get(3).text());
        nflPlayerGameLogDto.setTeam(columns.get(4).text());
        nflPlayerGameLogDto.setAway(columns.get(5).text().contains("@"));
        nflPlayerGameLogDto.setOpponent(columns.get(6).text());
        nflPlayerGameLogDto.setResult(columns.get(7).text());
        nflPlayerGameLogDto.setStartedGame(columns.get(8).text().contains("*"));

        if (nflPlayerGameLogDto instanceof WideReceiverGameLogDto) {
            ((WideReceiverGameLogDto) nflPlayerGameLogDto).setTargets(MapperUtils.getInteger(columns.get(9).text()));
            ((WideReceiverGameLogDto) nflPlayerGameLogDto)
                    .setReceptions(MapperUtils.getInteger(columns.get(10).text()));
            ((WideReceiverGameLogDto) nflPlayerGameLogDto).setYards(MapperUtils.getInteger(columns.get(11).text()));
            ((WideReceiverGameLogDto) nflPlayerGameLogDto).setYardsPerReception(columns.get(12).text());
            ((WideReceiverGameLogDto) nflPlayerGameLogDto)
                    .setReceivingTouchdowns(MapperUtils.getInteger(columns.get(13).text()));
            ((WideReceiverGameLogDto) nflPlayerGameLogDto).setCatchPercentage(columns.get(14).text());
            ((WideReceiverGameLogDto) nflPlayerGameLogDto).setYardsPerTarget(columns.get(15).text());
        }

        return nflPlayerGameLogDto;
    }

    public static NFLPlayerFantasyStatsDto mapNFLPlayerFantasyStatsRow(Elements columns, int playerRanking) {
        NFLPlayerFantasyStatsDto nflPlayerFantasyStatsDto = NFLPlayerFantasyStatsFactory
                .createNFLPlayer(columns.get(2).text());

        nflPlayerFantasyStatsDto.setRank(playerRanking);
        nflPlayerFantasyStatsDto
                .setPlayerName(columns.get(0).text().replace("*", StringUtils.EMPTY).replace("+", StringUtils.EMPTY));
        nflPlayerFantasyStatsDto.setPlayerUrl(
                URLUtils.SCRAPING_NFL_URL + MapperUtils.getPlayerUrl(columns.get(0)));
        int lastForwardSlash = nflPlayerFantasyStatsDto.getPlayerUrl().lastIndexOf("/") + 1;
        int lastPeriod = nflPlayerFantasyStatsDto.getPlayerUrl().lastIndexOf(".");
        nflPlayerFantasyStatsDto
                .setPlayerSystemName(nflPlayerFantasyStatsDto.getPlayerUrl().substring(lastForwardSlash, lastPeriod));
        nflPlayerFantasyStatsDto.setTeam(columns.get(1).text());
        nflPlayerFantasyStatsDto.setPosition(columns.get(2).text());

        // games played
        nflPlayerFantasyStatsDto.setAge(MapperUtils.getInteger(columns.get(3).text()));
        nflPlayerFantasyStatsDto.setGamesPlayed(MapperUtils.getInteger(columns.get(4).text()));
        nflPlayerFantasyStatsDto.setGamesStarted(MapperUtils.getInteger(columns.get(5).text()));
        // passing stats
        nflPlayerFantasyStatsDto.setPassesCompleted(MapperUtils.getInteger(columns.get(6).text()));
        nflPlayerFantasyStatsDto.setPassesAttempted(MapperUtils.getInteger(columns.get(7).text()));
        nflPlayerFantasyStatsDto.setPassingYards(MapperUtils.getInteger(columns.get(8).text()));
        nflPlayerFantasyStatsDto.setPassingTouchdowns(MapperUtils.getInteger(columns.get(9).text()));
        nflPlayerFantasyStatsDto.setInterceptions(MapperUtils.getInteger(columns.get(10).text()));
        // rushing stats
        nflPlayerFantasyStatsDto.setRushingAttempts(MapperUtils.getInteger(columns.get(11).text()));
        nflPlayerFantasyStatsDto.setRushingYards(MapperUtils.getInteger(columns.get(12).text()));
        nflPlayerFantasyStatsDto.setRushingYardsPerAttempt(columns.get(13).text());
        nflPlayerFantasyStatsDto.setRushingTouchdowns(MapperUtils.getInteger(columns.get(14).text()));
        // receiving stats
        nflPlayerFantasyStatsDto.setTargets(MapperUtils.getInteger(columns.get(15).text()));
        nflPlayerFantasyStatsDto.setReceptions(MapperUtils.getInteger(columns.get(16).text()));
        nflPlayerFantasyStatsDto.setReceivingYards(MapperUtils.getInteger(columns.get(17).text()));
        nflPlayerFantasyStatsDto.setReceivingYardsPerReception(columns.get(18).text());
        nflPlayerFantasyStatsDto.setReceivingTouchdowns(MapperUtils.getInteger(columns.get(19).text()));

        // if (nflPlayerFantasyStatsDto.getPosition().equalsIgnoreCase("RB")) {
        // mapNFLRunningBackFantasyStatsRow(columns,
        // (RunningBackFantasyStatsDto) nflPlayerFantasyStatsDto);
        // } else if (nflPlayerFantasyStatsDto.getPosition().equalsIgnoreCase("WR")) {
        // mapNFLWideReceiverFantasyStatsRow(columns,
        // (WideReceiverFantasyStatsDto) nflPlayerFantasyStatsDto);
        // } else if (nflPlayerFantasyStatsDto.getPosition().equalsIgnoreCase("TE")) {
        // mapNFLTightEndFantasyStatsRow(columns,
        // (TightEndFantasyStatsDto) nflPlayerFantasyStatsDto);
        // } else {
        // return new NFLPlayerFantasyStatsDto();
        // }

        // fumbles
        nflPlayerFantasyStatsDto.setFumbles(MapperUtils.getInteger(columns.get(20).text()));
        nflPlayerFantasyStatsDto.setFumblesLost(MapperUtils.getInteger(columns.get(21).text()));

        // scoring
        nflPlayerFantasyStatsDto.setTouchdowns(MapperUtils.getInteger(columns.get(22).text()));
        nflPlayerFantasyStatsDto.setTwoPointConversionMade(MapperUtils.getInteger(columns.get(23).text()));
        nflPlayerFantasyStatsDto.setTwoPointConversionPasses(MapperUtils.getInteger(columns.get(24).text()));
        // fantasy
        nflPlayerFantasyStatsDto.setFantasyPoints(MapperUtils.getInteger(columns.get(25).text()));
        nflPlayerFantasyStatsDto.setFantasyPointsPpr(columns.get(26).text());
        nflPlayerFantasyStatsDto.setFantasyVbd(MapperUtils.getInteger(columns.get(29).text()));
        nflPlayerFantasyStatsDto.setFantasyPositionRank(MapperUtils.getInteger(columns.get(30).text()));
        nflPlayerFantasyStatsDto.setFantasyOverallRank(MapperUtils.getInteger(columns.get(31).text()));
        return nflPlayerFantasyStatsDto;
    }

    // private static RunningBackFantasyStatsDto
    // mapNFLRunningBackFantasyStatsRow(Elements columns,
    // RunningBackFantasyStatsDto runningBackFantasyStatsDto) {
    // // rushing stats
    // runningBackFantasyStatsDto.setRushingAttempts(MapperUtils.getInteger(columns.get(11).text()));
    // runningBackFantasyStatsDto.setRushingYards(MapperUtils.getInteger(columns.get(12).text()));
    // runningBackFantasyStatsDto.setRushingYardsPerAttempt(columns.get(13).text());
    // runningBackFantasyStatsDto.setRushingTouchdowns(MapperUtils.getInteger(columns.get(14).text()));

    // // receiving stats
    // runningBackFantasyStatsDto.setTargets(MapperUtils.getInteger(columns.get(15).text()));
    // runningBackFantasyStatsDto.setReceptions(MapperUtils.getInteger(columns.get(16).text()));
    // runningBackFantasyStatsDto.setReceivingYards(MapperUtils.getInteger(columns.get(17).text()));
    // runningBackFantasyStatsDto.setReceivingYardsPerReception(columns.get(18).text());
    // runningBackFantasyStatsDto.setReceivingTouchdowns(MapperUtils.getInteger(columns.get(19).text()));
    // return runningBackFantasyStatsDto;
    // }

    // private static WideReceiverFantasyStatsDto
    // mapNFLWideReceiverFantasyStatsRow(Elements columns,
    // WideReceiverFantasyStatsDto wideReceiverFantasyStatsDto) {
    // // rushing stats
    // wideReceiverFantasyStatsDto.setRushingAttempts(MapperUtils.getInteger(columns.get(11).text()));
    // wideReceiverFantasyStatsDto.setRushingYards(MapperUtils.getInteger(columns.get(12).text()));
    // wideReceiverFantasyStatsDto.setRushingYardsPerAttempt(columns.get(13).text());
    // wideReceiverFantasyStatsDto.setRushingTouchdowns(MapperUtils.getInteger(columns.get(14).text()));

    // // receiving stats
    // wideReceiverFantasyStatsDto.setTargets(MapperUtils.getInteger(columns.get(15).text()));
    // wideReceiverFantasyStatsDto.setReceptions(MapperUtils.getInteger(columns.get(16).text()));
    // wideReceiverFantasyStatsDto.setReceivingYards(MapperUtils.getInteger(columns.get(17).text()));
    // wideReceiverFantasyStatsDto.setReceivingYardsPerReception(columns.get(18).text());
    // wideReceiverFantasyStatsDto.setReceivingTouchdowns(MapperUtils.getInteger(columns.get(19).text()));
    // return wideReceiverFantasyStatsDto;
    // }

    // private static TightEndFantasyStatsDto mapNFLTightEndFantasyStatsRow(Elements
    // columns,
    // TightEndFantasyStatsDto tightEndFantasyStatsDto) {
    // // rushing stats
    // tightEndFantasyStatsDto.setRushingAttempts(MapperUtils.getInteger(columns.get(11).text()));
    // tightEndFantasyStatsDto.setRushingYards(MapperUtils.getInteger(columns.get(12).text()));
    // tightEndFantasyStatsDto.setRushingYardsPerAttempt(columns.get(13).text());
    // tightEndFantasyStatsDto.setRushingTouchdowns(MapperUtils.getInteger(columns.get(14).text()));

    // // receiving stats
    // tightEndFantasyStatsDto.setTargets(MapperUtils.getInteger(columns.get(15).text()));
    // tightEndFantasyStatsDto.setReceptions(MapperUtils.getInteger(columns.get(16).text()));
    // tightEndFantasyStatsDto.setReceivingYards(MapperUtils.getInteger(columns.get(17).text()));
    // tightEndFantasyStatsDto.setReceivingYardsPerReception(columns.get(18).text());
    // tightEndFantasyStatsDto.setReceivingTouchdowns(MapperUtils.getInteger(columns.get(19).text()));
    // return tightEndFantasyStatsDto;
    // }

    // private static QuarterBackFantasyStatsDto
    // mapNFLQuarterBackFantasyStatsRow(Elements columns,
    // QuarterBackFantasyStatsDto quarterBackFantasyStatsDto) {
    // // passing stats
    // quarterBackFantasyStatsDto.setPassesCompleted(MapperUtils.getInteger(columns.get(6).text()));
    // quarterBackFantasyStatsDto.setPassesAttempted(MapperUtils.getInteger(columns.get(7).text()));
    // quarterBackFantasyStatsDto.setPassingYards(MapperUtils.getInteger(columns.get(8).text()));
    // quarterBackFantasyStatsDto.setPassingTouchdowns(MapperUtils.getInteger(columns.get(9).text()));
    // quarterBackFantasyStatsDto.setInterceptions(MapperUtils.getInteger(columns.get(10).text()));

    // // rushing stats
    // quarterBackFantasyStatsDto.setRushingAttempts(MapperUtils.getInteger(columns.get(11).text()));
    // quarterBackFantasyStatsDto.setRushingYards(MapperUtils.getInteger(columns.get(12).text()));
    // quarterBackFantasyStatsDto.setRushingYardsPerAttempt(columns.get(13).text());
    // quarterBackFantasyStatsDto.setRushingTouchdowns(MapperUtils.getInteger(columns.get(14).text()));
    // return quarterBackFantasyStatsDto;
    // }

    // public static WideReceiverGameLogDto mapNFLPlayerGameLogRow(Elements columns)
    // {
    // WideReceiverGameLogDto responseDto = new WideReceiverGameLogDto();
    // responseDto.setDate(columns.get(0).text());
    // responseDto.setGameCount(MapperUtils.getInteger(columns.get(1).text()));
    // responseDto.setWeek(MapperUtils.getInteger(columns.get(2).text()));
    // responseDto.setAge(columns.get(3).text());
    // responseDto.setTeam(columns.get(4).text());
    // // responseDto.setAway(getIsAway(columns.get(5).text()));
    // responseDto.setOpponent(columns.get(6).text());
    // responseDto.setResult(columns.get(7).text());
    // if (responseDto.getAge().isBlank()) {
    // responseDto.setReason(columns.get(8).text());
    // return responseDto;
    // }
    // responseDto.setStartedGame(columns.get(8).text().equalsIgnoreCase("*"));
    // return responseDto;
    // }

    public static String getNFLPlayerFantasyPosition(Elements playerAttributes) {
        String playerPosition = playerAttributes.get(2).text();
        if (!playerPosition.isBlank() && !playerPosition.equalsIgnoreCase("FantPos")) {
            return playerPosition;
        }
        return null;
    }
}
