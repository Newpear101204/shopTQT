package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
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

    @Column(name ="bestseller")
    private int bestseller;

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

    @ManyToMany(mappedBy = "products" , fetch = FetchType.EAGER )
    private List<Users> users = new ArrayList<>();



    @OneToMany(mappedBy ="product" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL )
    private List<Cart_Item> cart_items;

    @OneToMany(mappedBy ="product" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL)
    private List<Orders_item> orders_items;

    @OneToMany(mappedBy ="productt" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL )
    private List<ProductImage> productImages;


}
