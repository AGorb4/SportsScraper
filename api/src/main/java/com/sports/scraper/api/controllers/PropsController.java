package com.sports.scraper.api.controllers;

import com.sports.scraper.api.service.PropsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/props")
public class PropsController {

    @Autowired
    PropsService propsService;

    @GetMapping(path = "/types")
    public void getPropTypes() {
        propsService.getPropTypes();
    }
}
