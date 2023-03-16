package com.example.project.dto;


import com.example.project.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;


import java.time.LocalDate;

@Data
public class OrderDto {

    private Long id;
    @NotEmpty(message = "Destination name cannot be empty.")
    private String destination;
    @NotNull(message = "Delivery date cannot be empty.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deliveryDate;
    private OrderStatus status;
    private LocalDate lastUpdated;
}
