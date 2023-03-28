package com.example.project.service;

import com.example.project.entity.DestinationEntity;
import com.example.project.entity.OrderEntity;
import com.example.project.enums.OrderStatus;
import com.example.project.repository.OrderRepository;
import com.example.project.support.CurrentDateAndProfitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class AsyncShippingService {

    private final CurrentDateAndProfitService currentDateAndProfitService;
    private final OrderRepository orderRepository;

    public AsyncShippingService(CurrentDateAndProfitService currentDateAndProfitService, OrderRepository orderRepository) {
        this.currentDateAndProfitService = currentDateAndProfitService;
        this.orderRepository = orderRepository;
    }

    @Async("deliveryThread")
    public void startDeliveries(DestinationEntity destinationEntity, List<Long> orderIds) {

        try {
            Thread.sleep(destinationEntity.getDistance()*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Iterable<OrderEntity> allOrdersById = orderRepository.findAllById(orderIds);
        List<OrderEntity> filteredOrderEntities = StreamSupport.stream(allOrdersById.spliterator(), false)
                .filter(orderEntity -> orderEntity.getStatus() != OrderStatus.CANCELLED)
                .toList();

        String threadName = Thread.currentThread().getName().substring(14);
        log.info(String.format("Starting %s deliveries for %s on Thread-%s for %s km", filteredOrderEntities.size(),
                destinationEntity.getName(), threadName, destinationEntity.getDistance()));

        filteredOrderEntities.forEach(orderEntity -> {
            orderEntity.setStatus(OrderStatus.DELIVERED);
            orderEntity.setLastUpdated(currentDateAndProfitService.getCurrentDate());
            orderRepository.save(orderEntity);
        });

        Integer profitPerDayPerDestination = destinationEntity.getDistance() * filteredOrderEntities.size();
        currentDateAndProfitService.addProfit(profitPerDayPerDestination);

        log.info(String.format("%s deliveries completed for %s", filteredOrderEntities.size(),
                destinationEntity.getName()));


    }
}
