package com.example.project.controller;

import com.example.project.dto.OrderDto;
import com.example.project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public Map<String, List<OrderDto>> addOrders(@Valid @RequestBody List<OrderDto> orderDtos) {
        return orderService.addOrders(orderDtos);
    }


}
