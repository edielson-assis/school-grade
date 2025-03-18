package br.com.edielsonassis.course.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    
    UserDetails loadUserByUsername(String email);
}