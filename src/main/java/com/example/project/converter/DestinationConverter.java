package com.example.project.converter;

import com.example.project.dto.DestinationDto;
import com.example.project.dto.OrderDto;
import com.example.project.entity.DestinationEntity;
import com.example.project.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DestinationConverter {

    public DestinationEntity fromDtoToEntity(DestinationDto destinationDto) {
        DestinationEntity destinationEntity = new DestinationEntity();

        destinationEntity.setName(destinationDto.getName());
        destinationEntity.setDistance(destinationDto.getDistance());
//        destinationEntity.setOrders(destinationDto.getOrders());   //TODO fix this

        return destinationEntity;
    }



    public DestinationDto fromEntityToDto(DestinationEntity destinationEntity) {
        DestinationDto destinationDto = new DestinationDto();

        destinationDto.setId(destinationEntity.getId());
        destinationDto.setName(destinationEntity.getName());
        destinationDto.setDistance(destinationEntity.getDistance());
//        destinationDto.setOrders(destinationEntity.getOrders());

        return destinationDto;
    }
}
