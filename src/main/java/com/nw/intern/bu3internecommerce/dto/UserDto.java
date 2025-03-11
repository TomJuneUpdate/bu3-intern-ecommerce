package com.nw.intern.bu3internecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nw.intern.bu3internecommerce.entity.Address;
import com.nw.intern.bu3internecommerce.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Builder
public class UserDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Date dateOfBirth;
    private Set<Address> addresses = new HashSet<Address>();
    private Role role;
}
