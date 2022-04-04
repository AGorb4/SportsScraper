package com.sports.scraper.api.controllers;

import java.util.List;

import com.sports.scraper.api.service.PropsService;
import com.sports.scraper.domain.props.draftkings.responses.OfferCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/props")
public class PropsController {

    @Autowired
    PropsService propsService;

    @GetMapping(path = "/types")
    public ResponseEntity<List<OfferCategory>> getPropTypes() {
        return ResponseEntity.ok(propsService.getPropTypes());
    }
}
