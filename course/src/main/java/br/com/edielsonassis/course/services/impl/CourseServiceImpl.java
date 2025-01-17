package br.com.edielsonassis.course.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.edielsonassis.course.models.CourseModel;
import br.com.edielsonassis.course.repositories.CourseRepository;
import br.com.edielsonassis.course.repositories.LessonRepository;
import br.com.edielsonassis.course.repositories.ModuleRepository;
import br.com.edielsonassis.course.services.CourseService;
import br.com.edielsonassis.course.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Override
    public void deleteCourse(UUID courseId) {
        var course = findCourseById(courseId);
        var modules = moduleRepository.findAllModulesIntoCourse(course.getCourseId());
        
        modules.forEach(module -> {
            var lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
            if (!lessons.isEmpty()) {
                log.info("Deleting all lessons in module with id: {}", module.getModuleId());
                lessonRepository.deleteAll(lessons);
            }
        });

        if (!modules.isEmpty()) {
            log.info("Deleting all modules in course with id: {}", courseId);
            moduleRepository.deleteAll(modules);
        }
        log.info("Deleting course with id: {}", courseId);
        courseRepository.delete(course);
    }

    private CourseModel findCourseById(UUID id) {
        log.info("Verifying course by id: {}", id);
        return courseRepository.findById(id).orElseThrow(() -> {
            log.error("Course not found: {}", id);
            return new ObjectNotFoundException("Course not found: " + id);
        });    
    }
}