package com.example.Personal.Data.Management.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    @NotEmpty
    @Size(min=1, message = "phone number must be 11 characters")
    @Column(unique=true)
    private String id;
    private Set<RoleDto> roles = new HashSet<>();
}
