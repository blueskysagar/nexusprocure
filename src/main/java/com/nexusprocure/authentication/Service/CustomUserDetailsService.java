package com.nexusprocure.authentication.Service;

import com.nexusprocure.authentication.security.CustomUserPrincipal;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> optionalUser = userRepository.findByEmail(username);
       User user = optionalUser.orElseThrow(()  -> new UsernameNotFoundException("User not found"));
       return new CustomUserPrincipal(user);


    }


}
