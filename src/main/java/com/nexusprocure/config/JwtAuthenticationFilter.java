package com.nexusprocure.config;

import com.nexusprocure.authentication.Service.CustomUserDetailsService;
import com.nexusprocure.authentication.Service.JwtService;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.hibernate.annotations.Comment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yaml.snakeyaml.scanner.ScannerImpl;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService, CustomAuthenticationEntryPoint authenticationEntryPoint){
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
        }
        //if token exists and is invalid block the request
        if (token != null && !jwtService.isTokenValid(token)){
          authenticationEntryPoint.commence(request,
                  response,
                  new BadCredentialsException("Invalid or expired JWT token")

          );
          return;

        }
        // if token is valid  set authentication in securityContext
        if(token!= null ){
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null,
                    userDetails.getAuthorities());
            //Store authenticated user in spring security
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);
    }




}
