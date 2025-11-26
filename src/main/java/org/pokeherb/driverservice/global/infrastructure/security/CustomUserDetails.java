package org.pokeherb.driverservice.global.infrastructure.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID userId;
    private final String username;
    private final String name;
    private final String email;
    private final String roles;
    private final Long hubId;
    private final UUID vendorId;

    @Builder
    public CustomUserDetails(UUID userId, String username, String name, String email, String roles, Long hubId, UUID vendorId) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.hubId = hubId;
        this.vendorId = vendorId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> userRoles = StringUtils.hasText(roles) ? Arrays.stream(roles.split(",")).toList() : List.of("ROLE_USER");
        return userRoles.stream()
                .map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
