package com.example.shop.config;


import com.example.shop.filter.JwtTokenFilter;
import com.example.shop.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    "/shopqtq/login","/shopqtq/register", "/shopqtq/allproducts" ,
                                    "/shopqtq/searchproducts", "shopqtq/chatbot"
                            )
                            .permitAll()
                            .requestMatchers(POST,"/shopqtq/createproduct").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/shopqtq/deleteproduct/{id}").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/shopqtq/deleteproductimage").hasAnyRole("ADMIN")
                            .requestMatchers(POST,"/shopqtq/cart").hasAnyRole("CUS")
                            .requestMatchers(GET,"/shopqtq/cart").hasAnyRole("CUS")
                            .requestMatchers(GET,"/shopqtq/order").hasAnyRole("CUS")
                            .requestMatchers(GET,"/shopqtq/cart").hasAnyRole("CUS")
                            .requestMatchers(POST,"/api/vnpay/create-payment").hasAnyRole("CUS")
                            .requestMatchers(GET,"/api/vnpay/vnpay-return").permitAll() // Cho phép truy cập callback mà không cần auth
                            .anyRequest().authenticated();

                });
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Địa chỉ FE
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Cho phép gửi cookie hoặc thông tin xác thực

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}
