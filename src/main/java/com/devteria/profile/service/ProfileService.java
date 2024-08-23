package com.devteria.profile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devteria.profile.dto.identity.Credential;
import com.devteria.profile.dto.identity.TokenExchangeParam;
import com.devteria.profile.dto.identity.UserCreationParam;
import com.devteria.profile.dto.request.RegistrationRequest;
import com.devteria.profile.dto.response.ProfileResponse;
import com.devteria.profile.exception.AppException;
import com.devteria.profile.exception.ErrorCode;
import com.devteria.profile.exception.ErrorNormalizer;
import com.devteria.profile.mapper.ProfileMapper;
import com.devteria.profile.repository.IdentityClient;
import com.devteria.profile.repository.ProfileRepository;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Qualifier
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    IdentityClient identityClient;
    ErrorNormalizer errorNormalizer;

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    public ProfileResponse getMyProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();
        var profile =
                profileRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return profileMapper.toProfileResponse(profile);
    }

    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    public ProfileResponse register(RegistrationRequest request) {

        try {
            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .grant_type("client_credentials")
                    .build());

            var creationResponse = identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(request.getUsername())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .email(request.getEmail())
                            .emailVerified(false)
                            .enabled(true)
                            .credentials(List.of(Credential.builder()
                                    .temporary(false)
                                    .type("password")
                                    .value(request.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);

            var profile = profileMapper.toProfile(request);
            profile.setUserId(userId);

            profile = profileRepository.save(profile);

            log.info("profile {}", profile);

            return profileMapper.toProfileResponse(profile);

        } catch (FeignException e) {

            throw errorNormalizer.handleKeyCloakEx(e);
        }
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        if (location == null) {
            return null;
        }
        String[] splitStr = location.split("/");
        return splitStr[splitStr.length - 1];
    }
}
