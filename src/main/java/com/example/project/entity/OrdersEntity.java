package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "orders")
@Data
public class OrdersEntity {
    @Id
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id", nullable = false)
    private DestinationEntity destination;
    private String delivery_date;
    private String status;
    private String last_updated;


}
