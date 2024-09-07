package com.devteria.profile.configuration;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String REAL_ACCESS = "realm_access";

    private final String PREFIX_ROLE = "ROLE_";

    private final String ROLES = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {

        Map<String, Object> realmAccessMap = source.getClaimAsMap(REAL_ACCESS);

        Object roles = realmAccessMap.get(ROLES);

        if (roles instanceof List roleList) {
            return ((List<String>) roleList)
                    .stream()
                            .map(s -> new SimpleGrantedAuthority(PREFIX_ROLE + s))
                            .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
