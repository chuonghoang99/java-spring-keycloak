package com.devteria.profile.mapper;

import com.devteria.profile.dto.request.RegistrationRequest;
import com.devteria.profile.dto.response.ProfileResponse;
import com.devteria.profile.entity.Profile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public Profile toProfile(RegistrationRequest request) {
        if ( request == null ) {
            return null;
        }

        Profile.ProfileBuilder profile = Profile.builder();

        profile.email( request.getEmail() );
        profile.username( request.getUsername() );
        profile.firstName( request.getFirstName() );
        profile.lastName( request.getLastName() );
        profile.dob( request.getDob() );

        return profile.build();
    }

    @Override
    public ProfileResponse toProfileResponse(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileResponse.ProfileResponseBuilder profileResponse = ProfileResponse.builder();

        profileResponse.id( profile.getId() );
        profileResponse.userId( profile.getUserId() );
        profileResponse.email( profile.getEmail() );
        profileResponse.username( profile.getUsername() );
        profileResponse.firstName( profile.getFirstName() );
        profileResponse.lastName( profile.getLastName() );
        profileResponse.dob( profile.getDob() );

        return profileResponse.build();
    }
}
