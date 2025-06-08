package com.example.shop.service.impl;

import com.example.shop.entity.MyUserDetail;
import com.example.shop.entity.Users;
import com.example.shop.repository.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roles = Arrays.asList(user.getRoles().split(" "));
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        }
        MyUserDetail myUserDetail = new MyUserDetail(username,user.getPassword(),true , true,true,true,authorities);
        BeanUtils.copyProperties(user, myUserDetail);
        return myUserDetail;
    }
}