package com.example.shop.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name ="orders")
public class Orders extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="status")
    private String status;

    @Column(name ="total")
    private int total;

    @Column(name ="shipping_adress")
    private String shippingAdress;

    @ManyToOne
    @JoinColumn(name ="users_id")
    private Users users;

    @OneToMany(mappedBy ="orders" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<Orders_item> orders_items;
}
