package br.com.edielsonassis.course.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.edielsonassis.course.models.LessonModel;
import br.com.edielsonassis.course.repositories.LessonRepository;
import br.com.edielsonassis.course.services.LessonService;
import br.com.edielsonassis.course.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository repository;

    @Override
    public void deleteLesson(UUID lessonId) {
        var lesson = findLessonById(lessonId);
        log.info("Deleting lesson with id: {}", lessonId);
        repository.delete(lesson);
    }
    
    private LessonModel findLessonById(UUID id) {
        log.info("Verifying lesson by id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.error("Lesson not found: {}", id);
            return new ObjectNotFoundException("Lesson not found: " + id);
        });    
    }
}