package br.com.edielsonassis.course.configs.security;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.edielsonassis.course.services.AuthService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private static final String BEARER = "Bearer ";

    private final AuthService authService;
    private Algorithm algorithm;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }
    
    public Authentication getAuthentication(String token) {
		log.info("Getting authentication from token");
        DecodedJWT decodedJWT = verifyToken(token);
        UserDetails userDetails = authService.loadUserByUsername(decodedJWT.getSubject());
        log.debug("User authenticated: {}", decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
		log.info("Resolving token from request header");
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
		log.info("Validating token");
        DecodedJWT decodedJWT = verifyToken(token);
        return !decodedJWT.getExpiresAt().before(new Date());
    }

    private DecodedJWT verifyToken(String token) {
		log.info("Decoding token");
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}