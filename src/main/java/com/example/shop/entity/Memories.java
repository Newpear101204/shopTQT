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
@Table(name ="memories")
public class Memories  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="capacity")
    private String capacity;

    @ManyToMany(mappedBy = "memories" , fetch = FetchType.EAGER )
    private List<Product> building = new ArrayList<>();
}
