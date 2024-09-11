package com.devteria.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.devteria.profile.properties.MinioProperties;
import com.devteria.profile.properties.RedisProperties;

// Chuonghoang99
@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({MinioProperties.class, RedisProperties.class})
public class ProfileServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class, args);
    }
}
