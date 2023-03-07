package com.example.project.entity;

import com.example.project.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity(name = "orders")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private DestinationEntity destination;
    private LocalDate deliveryDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDate lastUpdated;


}
