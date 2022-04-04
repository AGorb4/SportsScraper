package com.sports.scraper.domain.props.draftkings.responses;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Offer implements Serializable {

    public long providerOfferId;
    public long providerEventId;
    public boolean isSuspended;
    public long offerSubcategoryId;
    public int betOfferTypeId;
    public List<Outcome> outcomes;
}
