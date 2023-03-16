package com.example.project.dto;

import com.example.project.entity.OrderEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
@Data
public class DestinationDto {

    private Long id;
    @NotEmpty(message = "Destination name cannot be null")
    private String name;
    @NotNull(message = "The distance cannot be null")
    private Integer distance;
//    @NotEmpty(message = "At least one order must be added")
    private List<OrderDto> Orders;
}
