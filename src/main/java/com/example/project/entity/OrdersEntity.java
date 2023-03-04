package com.example.project.entity;

import com.example.project.enums.OrderStatus;
import jakarta.persistence.*;

@Entity(name = "orders")
public class OrdersEntity {
    @Id
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id", nullable = false)
    private DestinationEntity destination;
    private String delivery_date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String last_updated;


}
