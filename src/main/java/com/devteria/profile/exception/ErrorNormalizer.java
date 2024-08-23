package com.devteria.profile.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.devteria.profile.dto.identity.KeyCloakError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorNormalizer {

    private final ObjectMapper objectMapper;

    private final Map<String, ErrorCode> errorCodeMap;

    public ErrorNormalizer() {
        objectMapper = new ObjectMapper();
        errorCodeMap = new HashMap<>();

        errorCodeMap.put("User exists with same email", ErrorCode.EMAIL_EXITS);
    }

    public AppException handleKeyCloakEx(FeignException exception) {

        try {
            log.warn("Cannot complete requets", exception);
            var response = objectMapper.readValue(exception.contentUTF8(), KeyCloakError.class);

            String errorMes = response.getErrorMessage();

            if (Objects.nonNull(errorMes) && Objects.nonNull(errorCodeMap.get(errorMes))) {
                return new AppException(errorCodeMap.get(errorMes));
            }

            return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        } catch (JsonProcessingException ex) {
            log.error("Cannot deserialize content", ex);
        }

        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
