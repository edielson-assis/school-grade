package br.com.edielsonassis.course.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.course.dtos.request.CourseRequest;
import br.com.edielsonassis.course.dtos.response.CourseResponse;
import br.com.edielsonassis.course.models.CourseModel;

public interface CourseService {

    CourseResponse saveCourse(CourseRequest courseRequest);

    Page<CourseResponse> findAllCourses(Integer page, Integer size, String direction, Specification<CourseModel> spec);
    
    CourseResponse findCourseById(UUID courseId);

    void deleteCourseById(UUID courseId);

    CourseResponse updateCourseById(UUID courseId, CourseRequest courseRequest);

    CourseResponse updateCourseImageById(UUID courseId, CourseRequest courseRequest);
}