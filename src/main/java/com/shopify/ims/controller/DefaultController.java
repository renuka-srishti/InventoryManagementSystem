package com.shopify.ims.controller;

import com.shopify.ims.service.DummyDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class DefaultController {

    private final DummyDataService service;

    /**
     * This API creates a dummy data to test this server.
    */
    @GetMapping("/dummyData")
    public String addDummyInventory() {
        log.debug("Dummy Data Created");
        service.addDummyData();
        return "redirect:/";
    }

    /**
     * Default path for this server.
     */
    @GetMapping("/")
    public String landingPage() {
        return "index";
    }
}
