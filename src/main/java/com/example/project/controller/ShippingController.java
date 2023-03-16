package com.example.project.controller;

import com.example.project.service.ShippingService;
import com.example.project.support.CurrentDateAndProfitService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping")
@Validated
public class ShippingController {

    private final ShippingService shippingService;
    private final CurrentDateAndProfitService currentDateAndProfitService;

    public ShippingController(ShippingService shippingService, CurrentDateAndProfitService currentDateAndProfitService) {
        this.shippingService = shippingService;
        this.currentDateAndProfitService = currentDateAndProfitService;
    }

    @PostMapping("/new-day")
    public ResponseEntity<String> startNewDay() {
        shippingService.startDay();
        return ResponseEntity.ok("New day starting : " + currentDateAndProfitService.getCurrentDate());
    }
}
