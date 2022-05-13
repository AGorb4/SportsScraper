package com.sports.scraper.api.service;

import java.util.List;

import com.sports.scraper.domain.props.draftkings.responses.Event;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsDto;

public interface PropsService {

    List<OfferCategory> getPropTypes();

    List<Event> getEvents();

    PlayerPropStatisticsDto getPlayerPropStatistics(String playerName, String propType, float propTotal,
            int lastNInput,
            String againstTeam,
            int year);
}
