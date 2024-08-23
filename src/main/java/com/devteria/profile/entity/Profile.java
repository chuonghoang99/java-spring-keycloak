package com.devteria.profile.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Profile extends AbstractEntity<String> {

    // UserId from keycloak
    String userId;
    String email;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
}
