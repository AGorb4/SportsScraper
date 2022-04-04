package com.sports.scraper.domain.props.draftkings.responses;

import java.io.Serializable;

import lombok.Data;

@Data
public class Outcome implements Serializable {

    public long providerOfferId;
    public String label;
    public String oddsAmerican;
    public String oddsFractional;
    public String line;
}
