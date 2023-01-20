package com.example.Personal.Data.Management.security;

import com.example.Personal.Data.Management.exceptions.ResourceNotFoundException;
import com.example.Personal.Data.Management.models.User;
import com.example.Personal.Data.Management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("User", "email" , username));
        return user;
    }
}
