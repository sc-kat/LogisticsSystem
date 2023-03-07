package com.example.project.dto;


import com.example.project.enums.OrderStatus;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;


import java.time.LocalDate;

@Data
public class OrderDto {

    private Long id;
    @NotEmpty(message = "Destination name cannot be empty.")
    private String destination;
    @NotEmpty(message = "Delivery date cannot be empty.")
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
//    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deliveryDate;
//    @NotEmpty(message = "The status cannot be empty.")
    private OrderStatus status;
    private LocalDate lastUpdated;
}
