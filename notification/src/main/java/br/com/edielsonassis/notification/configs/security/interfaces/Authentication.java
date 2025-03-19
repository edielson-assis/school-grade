package br.com.edielsonassis.notification.configs.security.interfaces;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface Authentication extends UserDetails {

    Collection<? extends GrantedAuthority> getAuthorities();

    default String getUsername() {
        return null;        
    }

    default String getPassword() {
        return null;
    }
}