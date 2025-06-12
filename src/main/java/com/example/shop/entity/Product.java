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

    @OneToMany(mappedBy ="product" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<ProductImage> images;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Memories getMemories() {
        return memories;
    }

    public void setMemories(Memories memories) {
        this.memories = memories;
    }

    public List<Cart_Item> getCart_items() {
        return cart_items;
    }

    public void setCart_items(List<Cart_Item> cart_items) {
        this.cart_items = cart_items;
    }

    public List<Orders_item> getOrders_items() {
        return orders_items;
    }

    public void setOrders_items(List<Orders_item> orders_items) {
        this.orders_items = orders_items;
    }
}
