package com.sports.scraper.api.service;

import java.util.ArrayList;
import java.util.List;

import com.sports.scraper.domain.props.draftkings.responses.DraftkingsResponse;
import com.sports.scraper.domain.props.draftkings.responses.EventGroup;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;
import com.sports.scraper.domain.props.draftkings.responses.SubCategory;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PropsServiceImpl implements PropsService {

    private String draftkingsUrl = "https://sportsbook.draftkings.com//sites/US-SB/api/v4/eventgroups/88670846";

    @Override
    public List<OfferCategory> getPropTypes() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<DraftkingsResponse> response = restTemplate.getForEntity(draftkingsUrl,
                DraftkingsResponse.class);
        if (response.getStatusCode().value() == 200 && response.hasBody()) {
            System.out.println("Success getting draftkings offer categories");
            DraftkingsResponse draftkingsResponse = response.getBody();
            if (draftkingsResponse != null) {
                if (draftkingsResponse.getEventGroup() != null) {
                    for (OfferCategory category : draftkingsResponse.getEventGroup().getOfferCategories()) {
                        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty())
                            category.setSubCategories(new ArrayList<>());
                    }
                    return draftkingsResponse.getEventGroup().getOfferCategories();
                } else {
                    System.out.println("Draftkings event group object is null");
                }
            } else {
                System.out.println("Null response body getting draftkings offer categories");
            }
        }
        return new ArrayList<>();
    }

}
