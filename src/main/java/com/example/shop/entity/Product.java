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


    @ManyToOne
    @JoinColumn(name = "brands_id")
    private Brand brand;



    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Categories categories;

    @ManyToMany(fetch =  FetchType.LAZY )
    @JoinTable(name = "product_memories",
            joinColumns = @JoinColumn(name = "product_id" , nullable = false),
            inverseJoinColumns =  @JoinColumn(name ="memories_id", nullable= false))
    private List<Memories> memories;



    @OneToMany(mappedBy ="product" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<Cart_Item> cart_items;

    @OneToMany(mappedBy ="product" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<Orders_item> orders_items;

    @OneToMany(mappedBy ="productt" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<ProductImage> productImages;


}
