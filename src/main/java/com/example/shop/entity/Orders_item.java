package com.example.shop.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name ="orders_item")
public class Orders_item extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="quantity")
    private int quantity;

    @Column(name ="memories_id")
    private Long memoriesId;

    @Column(name ="price")
    private Float price ;

    @ManyToOne
    @JoinColumn(name ="orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name ="products_id")
    private Product product;


}
