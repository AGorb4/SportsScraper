package com.sports.scraper.api.service.props;

import java.util.List;

import com.sports.scraper.domain.props.draftkings.responses.Event;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsDto;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsReportRequest;
import com.sports.scraper.domain.props.statistics.responses.PropStatisticsReportResponse;

public interface PropsService {

        List<OfferCategory> getPropTypes();

        List<Event> getEvents(String league);

        PlayerPropStatisticsDto getPlayerPropStatistics(String league, String playerName, String propType,
                        float propTotal,
                        int lastNInput,
                        String againstTeam,
                        int year,
                        boolean includeGamelog);

        List<PropStatisticsReportResponse> getPlayerPropStatisticsReport(
                        PlayerPropStatisticsReportRequest playerPropStatisticsReportRequest);
}
