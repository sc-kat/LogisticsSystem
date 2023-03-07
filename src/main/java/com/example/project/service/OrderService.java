package com.example.project.service;

import com.example.project.converter.OrderConverter;
import com.example.project.dto.OrderDto;
import com.example.project.entity.OrderEntity;
import com.example.project.repository.DestinationRepository;
import com.example.project.repository.OrderRepository;
import com.example.project.support.CurrentDateAndProfitService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final DestinationRepository destinationRepository;
    private final CurrentDateAndProfitService currentDateAndProfitService;

    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter,
                        DestinationRepository destinationRepository,
                        CurrentDateAndProfitService currentDateAndProfitService) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.destinationRepository = destinationRepository;
        this.currentDateAndProfitService = currentDateAndProfitService;
    }

    public Map<String, List<OrderDto>> addOrders(List<OrderDto> orderDtos) {
        List<OrderDto> validOrders = new ArrayList<>();
        List<OrderDto> invalidOrders = new ArrayList<>();

        orderDtos.forEach(orderDto -> {
            if(orderDto.getDeliveryDate().isAfter(currentDateAndProfitService.getCurrentDate()) &&
                    destinationRepository.findByName(orderDto.getDestination()).isPresent()) {
                validOrders.add(orderDto);
            } else {
                invalidOrders.add(orderDto);
            }

        });
        List<OrderEntity> orderEntities = validOrders.stream()
                .map(orderConverter::fromDtoToEntity)
                .toList();

        Iterable<OrderEntity> savedEntities = orderRepository.saveAll(orderEntities);
        List<OrderEntity> entitiesListToConvert = StreamSupport.stream(savedEntities.spliterator(),false).toList();
        List<OrderDto> orderDtosToReturn = entitiesListToConvert.stream()
                .map(orderEntity -> orderConverter.fromEntityToDto(orderEntity))
                .toList();

        Map<String, List<OrderDto>> responseToAdd = new HashMap<>();
        responseToAdd.put("Added orders", orderDtosToReturn);
        responseToAdd.put("Invalid orders", invalidOrders);

        return responseToAdd;

    }
}
