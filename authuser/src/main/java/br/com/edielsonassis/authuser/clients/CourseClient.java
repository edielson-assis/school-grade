package br.com.edielsonassis.authuser.clients;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.edielsonassis.authuser.dtos.response.CourseResponse;
import br.com.edielsonassis.authuser.dtos.response.PageResponse;
import br.com.edielsonassis.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class CourseClient {

    @Value("${schoolgrade.api.url.course}")
    private String REQUEST_URL_COURSE;

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    @CircuitBreaker(name = "circuitbreakerInstance")
    public Page<CourseResponse> getAllCoursesByUser(UUID userId, Pageable pageable, String token) {
        final String url = REQUEST_URL_COURSE + utilsService.createUrlGetAllCoursesByUser(userId, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        log.info("Starting request to Course Microservice for userId: {} with URL: {}", userId, url);
        ParameterizedTypeReference<PageResponse<CourseResponse>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<PageResponse<CourseResponse>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), responseType);
        PageResponse<CourseResponse> pageResponse = response.getBody();
        log.info("Ending request to Course Microservice for userId: {}", userId);
        return pageResponse;
    }
}