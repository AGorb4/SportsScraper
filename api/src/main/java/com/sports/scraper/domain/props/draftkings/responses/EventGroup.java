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
public class EventGroup implements Serializable {
    private long eventGroupId;
    private long providerEventGroupId;
    private int providerId;
    private List<OfferCategory> offerCategories;
}
