package br.com.edielsonassis.course.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.course.dtos.request.LessonRequest;
import br.com.edielsonassis.course.dtos.response.LessonResponse;
import br.com.edielsonassis.course.models.LessonModel;

public interface LessonService {
    
    LessonResponse saveLesson(UUID moduleId, LessonRequest lessonRequest);

    Page<LessonResponse> findAllLessons(Integer page, Integer size, String direction, Specification<LessonModel> spec);
    
    LessonResponse findLessonById(UUID moduleId, UUID lessonId);

    LessonResponse updateLessonById(UUID moduleId, UUID lessonId, LessonRequest lessonRequest);
    
    String deleteLessonById(UUID moduleId, UUID lessonId);
}