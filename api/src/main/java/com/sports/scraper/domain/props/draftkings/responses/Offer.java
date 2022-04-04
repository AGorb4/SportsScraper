package com.sports.scraper.domain.props.draftkings.responses;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class Offer implements Serializable {

    private long providerOfferId;
    private long providerEventId;
    private boolean isSuspended;
    private long offerSubcategoryId;
    private int betOfferTypeId;
    private List<Outcome> outcomes;
}
