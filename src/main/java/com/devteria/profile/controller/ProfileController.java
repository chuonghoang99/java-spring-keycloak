package com.devteria.profile.controller;

import com.devteria.profile.dto.ApiResponse;
import com.devteria.profile.dto.request.RegistrationRequest;
import com.devteria.profile.dto.response.ProfileResponse;
import com.devteria.profile.repository.IdentityClient;
import com.devteria.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileController {
    ProfileService profileService;

    IdentityClient identityClient;

    @PostMapping("/register")
    ApiResponse<ProfileResponse> register(@RequestBody @Valid RegistrationRequest request) {

        var result = profileService.register(request);

        return ApiResponse.<ProfileResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/profiles")
    ApiResponse<List<ProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.getAllProfiles())
                .build();
    }

    @GetMapping("/my-profile")
    ApiResponse<ProfileResponse> getMyProfile() {
        var  result = profileService.getMyProfile();
        return ApiResponse.<ProfileResponse>builder()
                .result(result).build();
    }
}
