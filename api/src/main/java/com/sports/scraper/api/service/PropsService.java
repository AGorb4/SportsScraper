package com.sports.scraper.api.service;

import java.util.List;

import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;

public interface PropsService {

    List<OfferCategory> getPropTypes();
}
