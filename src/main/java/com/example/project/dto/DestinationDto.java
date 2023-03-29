package com.example.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DestinationDto {
    @Min(value = 1)
    private Long id;
    @NotEmpty(message = "Destination name cannot be null")
    private String name;
    @NotNull(message = "The distance cannot be null")
    private Integer distance;
    private Integer numberOfOrders;
}
