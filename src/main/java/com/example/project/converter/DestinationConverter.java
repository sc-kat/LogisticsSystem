package com.example.project.converter;

import com.example.project.dto.DestinationDto;
import com.example.project.entity.DestinationEntity;
import org.springframework.stereotype.Component;

@Component
public class DestinationConverter {

    private final OrderConverter orderConverter;

    public DestinationConverter(OrderConverter orderConverter) {
        this.orderConverter = orderConverter;
    }

    public DestinationEntity fromDtoToEntity(DestinationDto destinationDto) {
        DestinationEntity destinationEntity = new DestinationEntity();

        destinationEntity.setName(destinationDto.getName());
        destinationEntity.setDistance(destinationDto.getDistance());

        return destinationEntity;
    }

    public DestinationDto fromEntityToDto(DestinationEntity destinationEntity) {
        DestinationDto destinationDto = new DestinationDto();

        destinationDto.setId(destinationEntity.getId());
        destinationDto.setName(destinationEntity.getName());
        destinationDto.setDistance(destinationEntity.getDistance());
        destinationDto.setNumberOfOrders(destinationEntity.getOrders().size());

        return destinationDto;
    }
}
