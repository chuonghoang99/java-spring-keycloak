package com.devteria.profile.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@ConfigurationProperties(prefix = "integration.redis", ignoreUnknownFields = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisProperties {
    String port;

    String host;
}
