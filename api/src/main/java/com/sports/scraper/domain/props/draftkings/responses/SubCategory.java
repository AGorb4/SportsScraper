package com.sports.scraper.domain.props.draftkings.responses;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SubCategory implements Serializable {

    public long subcategoryId;
    public String name;
    public List<Offer> offers;
}
