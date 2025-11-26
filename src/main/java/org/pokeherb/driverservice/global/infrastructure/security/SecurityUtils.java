package org.pokeherb.driverservice.global.infrastructure.security;

public interface SecurityUtils {

    boolean isPermitted(String role);

    String getCurrentUsername();
}
