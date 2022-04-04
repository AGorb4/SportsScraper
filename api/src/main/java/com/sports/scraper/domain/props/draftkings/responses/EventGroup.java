package com.sports.scraper.domain.props.draftkings.responses;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class EventGroup implements Serializable {
    public long eventGroundId;
    public long providerEventGroupId;
    public int providerId;
    public List<OfferCategory> offerCategories;
}
