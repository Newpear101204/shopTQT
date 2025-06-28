package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name ="cart_item")
public class Cart_Item extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="number")
    private int number;

    @Column(name ="memories_id")
    private Long memoriesId;


    @ManyToOne
    @JoinColumn(name ="products_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name ="users_id")
    private Users users;


}
