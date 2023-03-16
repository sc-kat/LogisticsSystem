package com.example.project.service;

import com.example.project.entity.DestinationEntity;
import com.example.project.entity.OrderEntity;
import com.example.project.enums.OrderStatus;
import com.example.project.repository.OrderRepository;
import com.example.project.support.CurrentDateAndProfitService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShippingService {

    private final CurrentDateAndProfitService currentDateAndProfitService;
    private final OrderRepository orderRepository;
    private final AsyncShippingService asyncShippingService;


    public ShippingService(CurrentDateAndProfitService currentDateAndProfitService, OrderRepository orderRepository,
                           AsyncShippingService asyncShippingService) {
        this.currentDateAndProfitService = currentDateAndProfitService;
        this.orderRepository = orderRepository;
        this.asyncShippingService = asyncShippingService;
    }

    public void startDay() {
        currentDateAndProfitService.incrementDate();
        LocalDate currentDate = currentDateAndProfitService.getCurrentDate();
        log.info(String.format("New day starting : %s", currentDate));

        List<OrderEntity> orderEntitiesToDeliverOnCurrentDate = orderRepository.findAllByDeliveryDate(currentDate).stream()
                .filter(orderEntity -> orderEntity.getStatus() != OrderStatus.CANCELLED)
                .toList();

        Map<DestinationEntity, List<OrderEntity>> ordersByDestination = orderEntitiesToDeliverOnCurrentDate.stream()
                .collect(Collectors.groupingBy(OrderEntity::getDestination));


        String stringMessage = ordersByDestination.keySet().stream()
                .map(DestinationEntity::getName)
                .collect(Collectors.joining(", "));

        log.info(String.format("Today we will be delivering to %s.", stringMessage));
        
        orderEntitiesToDeliverOnCurrentDate.forEach(orderEntity -> {
            orderEntity.setStatus(OrderStatus.DELIVERING);
            orderEntity.setLastUpdated(currentDate);
            orderRepository.save(orderEntity);
        });

        ordersByDestination.forEach((destinationEntity, orderEntityList) -> {
            List<Long> orderIds = orderEntityList.stream()
                    .map(OrderEntity::getId)
                    .toList();
            asyncShippingService.startDeliveries(destinationEntity,orderIds);
        });
    }
}