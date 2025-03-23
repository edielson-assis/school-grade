package br.com.edielsonassis.course.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final CorsConfig corsConfig;

    private static final String INSTRUCTOR = "INSTRUCTOR";
    private static final String STUDENT = "STUDENT";
    private static final String[] INSTRUCTOR_METHODS = {"/courses/**", "/module/**"};
    private static final String[] STUDENT_GET_METHODS = {"/courses", "/courses/{courseId}", "/course/{courseId}/module/**", "/module/{moduleId}/lesson/**"};
    private static final String[] STUDENT_POST_METHODS = {"/courses/{courseId}/users/**"};

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
            ROLE_ADMIN > ROLE_MODERATOR
            ROLE_MODERATOR > ROLE_INSTRUCTOR
            ROLE_INSTRUCTOR > ROLE_STUDENT
        """);
    }

    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler(RoleHierarchy roleHierarchy) {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        
        return http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, STUDENT_GET_METHODS).hasRole(STUDENT)
                        .requestMatchers(HttpMethod.POST, STUDENT_POST_METHODS).hasRole(STUDENT)
                        .requestMatchers(INSTRUCTOR_METHODS).hasRole(INSTRUCTOR)
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}