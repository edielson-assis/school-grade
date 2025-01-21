package br.com.edielsonassis.course.services.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.edielsonassis.course.dtos.request.CourseRequest;
import br.com.edielsonassis.course.dtos.response.CourseResponse;
import br.com.edielsonassis.course.mappers.CourseMapper;
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

    @Transactional
    @Override
    public CourseResponse saveCourse(CourseRequest courseRequest) {
        var courseModel = CourseMapper.toEntity(courseRequest);
        log.info("Registering a new Course: {}", courseModel.getName());
		courseRepository.save(courseModel);
        var courseResponse = new CourseResponse();
        BeanUtils.copyProperties(courseModel, courseResponse);
        getFormattedEnumValue(courseModel, courseResponse);
        return courseResponse;
    }

    @Override
    public Page<CourseResponse> findAllCourses(Integer page, Integer size, String direction, Specification<CourseModel> spec) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "courseId"));
        log.info("Listing all courses");
        return courseRepository.findAll(spec, pageable).map(courseModel -> {
            var courseResponse = new CourseResponse();
            BeanUtils.copyProperties(courseModel, courseResponse);
            getFormattedEnumValue(courseModel, courseResponse);
            return courseResponse;
        });
    }

    @Override
    public CourseResponse findCourseById(UUID courseId) {
        var courseModel = findById(courseId);
        var courseResponse = new CourseResponse();
        BeanUtils.copyProperties(courseModel, courseResponse);
        getFormattedEnumValue(courseModel, courseResponse);
        return courseResponse;
    }

    @Transactional
    @Override
    public void deleteCourseById(UUID courseId) {
        var course = findById(courseId);
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

    @Transactional
    @Override
    public CourseResponse updateCourseById(UUID courseId, CourseRequest courseRequest) {
        var courseModel = findById(courseId);
        courseModel = CourseMapper.toEntity(courseModel, courseRequest);
        log.info("Updating course with id: {}", courseId);
        courseRepository.save(courseModel);
        var courseResponse = new CourseResponse();
        BeanUtils.copyProperties(courseModel, courseResponse);
        getFormattedEnumValue(courseModel, courseResponse);
        return courseResponse;
    }

    @Transactional
    @Override
    public CourseResponse updateCourseImageById(UUID courseId, CourseRequest courseRequest) {
        var courseModel = findById(courseId);
        courseModel = CourseMapper.toEntityImage(courseModel, courseRequest);
        log.info("Updating image");
        courseRepository.save(courseModel);
        var courseResponse = new CourseResponse();
        BeanUtils.copyProperties(courseModel, courseResponse);
        getFormattedEnumValue(courseModel, courseResponse);
        return courseResponse;
    }

    private CourseModel findById(UUID id) {
        log.info("Verifying course by id: {}", id);
        return courseRepository.findById(id).orElseThrow(() -> {
            log.error("Course not found: {}", id);
            return new ObjectNotFoundException("Course not found: " + id);
        });    
    }

    private void getFormattedEnumValue(CourseModel courseModel, CourseResponse courseResponse) {
        courseResponse.setCourseStatus(courseModel.getCourseStatus().getStatus());
        courseResponse.setCourseLevel(courseModel.getCourseLevel().getLevel());
    }
}