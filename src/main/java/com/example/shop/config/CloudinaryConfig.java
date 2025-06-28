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
        config.put("cloud_name", "dsrrctluz");
        config.put("api_key", "987862452819624");
        config.put("api_secret", "vXxl7aRMQTVAOMsFWsoSsQ_05ag");
        return new Cloudinary(config);
    }
}
