package com.nexusprocure.config;

import com.nexusprocure.authentication.security.CustomUserPrincipal;
import com.nexusprocure.authentication.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor(){
        try{
            return Optional.of(SecurityUtils.getCurrentUsername());
        } catch (Exception ex) {
            return Optional.of("system");
        }

      
    }
}
