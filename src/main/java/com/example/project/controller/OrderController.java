package com.example.project.controller;

import com.example.project.dto.OrderDto;
import com.example.project.exception.DataNotFound;
import com.example.project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public Map<String, List<OrderDto>> addOrders(@Valid @RequestBody List<OrderDto> orderDtos) {
        return orderService.addOrders(orderDtos);
    }

    @PostMapping("/cancel")
    public Map<String, List<Long>> updateOrders(@Valid @RequestBody List<Long> idsToCancel) {
        return orderService.cancelOrders(idsToCancel);
    }

    @GetMapping("/status_endpoint")
    public List<OrderDto> statusEndpoint(@RequestParam(name = "date", required = false) LocalDate date,
                                         @RequestParam(name = "destination", required = false) String destination) throws DataNotFound {
        return orderService.getStatusesByDateAndDestination(date, destination);
    }








}
