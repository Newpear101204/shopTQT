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
@Table(name ="product")
public class Product extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name")
    private String name;

    @Column(name ="describes")
    private String describes;

    @Column(name ="price")
    private Float price;

    @Column(name ="images")
    private String images;


    @ManyToOne
    @JoinColumn(name = "brands_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Categories categories;

    @ManyToOne
    @JoinColumn(name = "memories_id")
    private Memories memories;

    @OneToMany(mappedBy ="product" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<Cart_Item> cart_items;

    @OneToMany(mappedBy ="product" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<Orders_item> orders_items;
}
