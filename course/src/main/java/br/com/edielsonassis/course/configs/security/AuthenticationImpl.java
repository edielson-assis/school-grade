package br.com.edielsonassis.course.configs.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.edielsonassis.course.configs.security.interfaces.Authentication;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationImpl implements Authentication {

    private static final long serialVersionUID = 1L;

    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public static AuthenticationImpl build(String username, List<String> roles) {
		List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		return new AuthenticationImpl(username, authorities);
	}	

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }    
}