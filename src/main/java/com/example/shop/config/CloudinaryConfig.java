package com.example.shop.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dmkxepkiw");
        config.put("api_key", "248851859778315");
        config.put("api_secret", "hFEwWUlpvqSDOG1PP5VlZ9oL9TU");
        return new Cloudinary(config);
    }
}
