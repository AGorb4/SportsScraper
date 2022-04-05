package com.sports.scraper.domain.props.draftkings.responses;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class Event implements Serializable {
    private long eventId;
    private long eventGroupId;
    private String eventGroupName;
    private long providerEventId;
    private String name;
    private Date startDate;
    private String teamName1;
    private String teamName2;
    private String teamShortName1;
    private String teamShortName2;
    private EventStatus eventStatus;
}
