package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name ="users")
@EntityListeners(AuditingEntityListener.class)
public class Users extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="username")
    private String username;

    @Column(name ="password")
    private String password;

    @Column(name ="email")
    private String email;

    @Column(name ="roles")
    private String roles;

    @Column(name ="phone")
    private String phone;

    @Column(name ="status")
    private Integer status;

   @OneToMany(mappedBy ="users" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL )
    private List<Cart_Item> cart_items;


    @ManyToMany(fetch =  FetchType.LAZY )
    @JoinTable(name = "cart_item",
            joinColumns = @JoinColumn(name = "users_id" , nullable = false),
            inverseJoinColumns =  @JoinColumn(name ="products_id", nullable= false))
    private List<Product> products ;

    @OneToMany(mappedBy ="users" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL )
    private List<Orders> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_"+this.getRoles()));
        authorityList.add(new SimpleGrantedAuthority("ROLE_CUS"));
        return authorityList;
    }

}
