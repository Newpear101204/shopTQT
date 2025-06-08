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
@Table(name ="memories")
public class Memories extends  BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="capacity")
    private String capacity;

    @OneToMany(mappedBy ="memories" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL , orphanRemoval =  true)
    private List<Product> products;
}
