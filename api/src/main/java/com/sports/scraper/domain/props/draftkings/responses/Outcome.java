package com.sports.scraper.domain.props.draftkings.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class Outcome implements Serializable {

    private long providerOfferId;
    private String label;
    private String oddsAmerican;
    private String oddsFractional;
    private String line;
}
