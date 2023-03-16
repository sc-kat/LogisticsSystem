package com.example.project.service;

import com.example.project.converter.OrderConverter;
import com.example.project.dto.OrderDto;
import com.example.project.entity.OrderEntity;
import com.example.project.enums.OrderStatus;
import com.example.project.exception.DataNotFound;
import com.example.project.repository.DestinationRepository;
import com.example.project.repository.OrderRepository;
import com.example.project.support.CurrentDateAndProfitService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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

    public Map<String, List<OrderDto>> addOrders(List<OrderDto> orderDtos) {                                                //TODO to add exceptions
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
                .map(orderConverter::fromEntityToDto)
                .toList();


        return Map.of("addedOrders", orderDtosToReturn,"invalidOrders", invalidOrders);
    }
    @Transactional
    public Map<String, List<Long>> cancelOrders(List<Long> idsToCancel) {
        List<Long> cancelledOrderIds = new ArrayList<>();
        List<Long> failedOrderIds = new ArrayList<>();


        idsToCancel.forEach(id -> {
            if (orderRepository.findById(id).isEmpty()) {
                failedOrderIds.add(id);
            }
        });

        Iterable<OrderEntity> allOrdersById = orderRepository.findAllById(idsToCancel);
        List<OrderEntity> orderEntitiesToUpdate = StreamSupport.stream(allOrdersById.spliterator(), false).toList();

        orderEntitiesToUpdate.forEach(orderEntity -> {
            if (orderEntity.getStatus() != OrderStatus.DELIVERED) {
                orderEntity.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(orderEntity);
                orderRepository.updateLastUpdated(currentDateAndProfitService.getCurrentDate(), orderEntity.getId());

                cancelledOrderIds.add(orderEntity.getId());


            } else  {
                failedOrderIds.add(orderEntity.getId());                                                                   //TODO send info to client with what was already delivered, what was incorrect, etc
            }
        });

        return Map.of("cancelledOrderIds", cancelledOrderIds, "failedOrdersIds", failedOrderIds);

    }

    public List<OrderDto> getStatusByDateAndDestination(LocalDate date, String destination) throws DataNotFound {

        if(date == null) {
            date = currentDateAndProfitService.getCurrentDate();
        }

        List<OrderEntity> ordersByDeliveryDate = orderRepository.findAllByDeliveryDate(date);

        if(ordersByDeliveryDate.isEmpty()){
            throw new DataNotFound("No orders were found.");
        } else {
            if (destination.isEmpty()) {

                return ordersByDeliveryDate.stream()
                        .map(orderConverter::fromEntityToDto)
                        .toList();
            } else {
                return ordersByDeliveryDate.stream()
                        .filter(orderEntity -> Objects.equals(orderEntity.getDestination().getName().toLowerCase(), destination))
                        .map(orderConverter::fromEntityToDto)
                        .toList();
            }
        }
    }
}

