package com.sports.scraper.domain.props.draftkings.responses;

import java.io.Serializable;
import java.util.List;

public class OfferCategory implements Serializable {

    public long offerCategoryId;
    public String name;
    public List<SubCategory> subCategories;
}
