package com.devteria.profile.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@ConfigurationProperties(prefix = "integration.minio", ignoreUnknownFields = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinioProperties {

    String accessKey;

    String secretKey;

    String url;
}
