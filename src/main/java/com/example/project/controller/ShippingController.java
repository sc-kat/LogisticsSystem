package com.example.project.controller;

import com.example.project.configuration.ExecutorConfig;
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
    private final ExecutorConfig executorConfig;


    public ShippingController(ShippingService shippingService, CurrentDateAndProfitService currentDateAndProfitService,
                              ExecutorConfig executorConfig) {
        this.shippingService = shippingService;
        this.currentDateAndProfitService = currentDateAndProfitService;
        this.executorConfig = executorConfig;
    }

    @PostMapping("/new-day")
    public ResponseEntity<String> startNewDay() {
        if(executorConfig.threadExecutor().getActiveCount() != 0) {
            return ResponseEntity.badRequest().body("Present day is not over. Please wait for all orders to be delivered.");
        } else {
            shippingService.startDay();
            return ResponseEntity.ok("New day starting : " + currentDateAndProfitService.getCurrentDate());
        }
    }
}
