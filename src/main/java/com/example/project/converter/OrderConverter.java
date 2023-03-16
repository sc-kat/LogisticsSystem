package com.example.project.converter;

import com.example.project.dto.DestinationDto;
import com.example.project.dto.OrderDto;
import com.example.project.entity.DestinationEntity;
import com.example.project.entity.OrderEntity;
import com.example.project.enums.OrderStatus;
import com.example.project.repository.DestinationRepository;
import com.example.project.support.CurrentDateAndProfitService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderConverter {

    private final DestinationRepository destinationRepository;
    private final CurrentDateAndProfitService currentDateAndProfitService;

    public OrderConverter(DestinationRepository destinationRepository, CurrentDateAndProfitService currentDateAndProfitService) {
        this.destinationRepository = destinationRepository;
        this.currentDateAndProfitService = currentDateAndProfitService;
    }

    public OrderEntity fromDtoToEntity(OrderDto orderDto) {
        OrderEntity order = new OrderEntity();

        order.setDestination(destinationRepository.findByName(orderDto.getDestination()).get());
        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setStatus(OrderStatus.NEW);
        order.setLastUpdated(currentDateAndProfitService.getCurrentDate());

        return order;

    }

    public List<OrderEntity> fromDtosToEntities(List<OrderDto> orderDtos) {
        return orderDtos.stream()
                .map(this::fromDtoToEntity)
                .toList();
    }

    public OrderDto fromEntityToDto(OrderEntity orderEntity) {
        OrderDto order = new OrderDto();

        order.setId(orderEntity.getId());
        order.setDestination(orderEntity.getDestination().getName());
        order.setDeliveryDate(orderEntity.getDeliveryDate());
        order.setStatus(orderEntity.getStatus());
        order.setLastUpdated(orderEntity.getLastUpdated());

        return order;

    }

    public List<OrderDto> fromEntitiesToDtos(List<OrderEntity> orderEntities) {
        return orderEntities.stream()
                .map(orderEntity -> fromEntityToDto(orderEntity))
                .toList();
    }
}
