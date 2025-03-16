package br.com.edielsonassis.authuser.utils.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.edielsonassis.authuser.models.UserModel;

public class AuthenticatedUser {
    
    public static UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserModel) authentication.getPrincipal();
    }
}