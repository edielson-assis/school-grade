package br.com.edielsonassis.authuser.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final JwtTokenProvider tokenProvider;
    private final CorsConfig corsConfig;

    private static final String ADMIN = "ADMIN";
    private static final String[] PUBLIC_METHODS = {"/auth/**"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        
        return http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers(PUBLIC_METHODS).permitAll()
                            .requestMatchers("/users/**").authenticated()
                            .requestMatchers("/admin/**").hasRole(ADMIN)).build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}