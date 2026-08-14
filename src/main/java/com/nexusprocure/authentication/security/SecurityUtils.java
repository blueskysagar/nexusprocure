package com.nexusprocure.authentication.security;

import com.nexusprocure.user.entity.Role;
import jakarta.persistence.SecondaryTable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class SecurityUtils {
    private SecurityUtils(){}
    public static CustomUserPrincipal getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserPrincipal) authentication.getPrincipal();
    }
    public static String getCurrentUsername(){return getCurrentUser().getUsername();}
    public static boolean hasRole(Role role){
        return hasAnyRole(role);
    }
    public static boolean hasAnyRole(Role... roles){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return false;
        }
        Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return Arrays.stream(roles)
                .map(role -> "ROLE_" + role.name())
                .anyMatch(authorities::contains);
    }




}
