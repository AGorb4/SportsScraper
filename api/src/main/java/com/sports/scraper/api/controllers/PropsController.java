package com.sports.scraper.api.controllers;

import java.util.List;

import com.sports.scraper.api.service.PropsService;
import com.sports.scraper.domain.props.draftkings.responses.Event;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsDto;
import com.sports.scraper.domain.props.statistics.responses.PlayerPropStatisticsReportRequest;
import com.sports.scraper.domain.props.statistics.responses.PropStatisticsReportResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/props")
public class PropsController {

    @Autowired
    PropsService propsService;

    @GetMapping(path = "/types")
    public ResponseEntity<List<OfferCategory>> getPropTypes() {
        return ResponseEntity.ok(propsService.getPropTypes());
    }

    @GetMapping(path = "/events")
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(propsService.getEvents());
    }

    @GetMapping(path = "/statistics/{year}/{propType}/{propTotal}/{playerName}/{lastNInput}")
    public ResponseEntity<PlayerPropStatisticsDto> getPlayerPropStatistics(@PathVariable String playerName,
            @PathVariable int year, @PathVariable String propType, @PathVariable float propTotal,
            @PathVariable int lastNInput) {
        System.out.println("Getting prop stats for " + playerName);
        return ResponseEntity.ok(
                propsService.getPlayerPropStatistics(playerName, propType, propTotal, lastNInput, "", year, true));
    }

    @PostMapping(path = "/statistics/report")
    public ResponseEntity<List<PropStatisticsReportResponse>> getPlayerPropStatisticsReport(
            @RequestBody PlayerPropStatisticsReportRequest playerPropStatisticsReportRequest) {
        return ResponseEntity.ok(propsService.getPlayerPropStatisticsReport(playerPropStatisticsReportRequest));
    }
}
