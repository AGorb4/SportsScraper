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
public class OfferCategory implements Serializable {

    private long offerCategoryId;
    private String name;
    private List<SubCategory> subCategories;
}
