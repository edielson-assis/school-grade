package br.com.edielsonassis.course.services.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.course.dtos.request.LessonRequest;
import br.com.edielsonassis.course.dtos.response.LessonResponse;
import br.com.edielsonassis.course.mappers.LessonMapper;
import br.com.edielsonassis.course.models.LessonModel;
import br.com.edielsonassis.course.models.ModuleModel;
import br.com.edielsonassis.course.repositories.LessonRepository;
import br.com.edielsonassis.course.repositories.ModuleRepository;
import br.com.edielsonassis.course.services.LessonService;
import br.com.edielsonassis.course.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public LessonResponse saveLesson(UUID moduleId, LessonRequest lessonRequest) {
        var module = findModuleById(moduleId);
        var lessonModel = LessonMapper.toEntity(lessonRequest, module);
        log.info("Registering a new Lesson: {}", lessonModel.getTitle());
		lessonRepository.save(lessonModel);
        var lessonResponse = new LessonResponse();
        BeanUtils.copyProperties(lessonModel, lessonResponse);
        return lessonResponse;
    }

    @Override
    public Page<LessonResponse> findAllLessons(Integer page, Integer size, String direction, Specification<LessonModel> spec) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "lessonId"));
        log.info("Listing all lessons");
        return lessonRepository.findAll(spec, pageable).map(lessonModel -> {
            var lessonResponse = new LessonResponse();
            BeanUtils.copyProperties(lessonModel, lessonResponse);
            return lessonResponse;
        });
    }

    @Override
    public LessonResponse findLessonById(UUID moduleId, UUID lessonId) {
        var lessonModel = getLessonById(moduleId, lessonId);
        var lessonResponse = new LessonResponse();
        BeanUtils.copyProperties(lessonModel, lessonResponse);
        return lessonResponse;
    }

    @Override
    public LessonResponse updateLessonById(UUID moduleId, UUID lessonId, LessonRequest lessonRequest) {
        var lessonModel = getLessonById(moduleId, lessonId);
        lessonModel = LessonMapper.toEntity(lessonModel, lessonRequest);
        log.info("Updating lesson with id: {}", lessonId);
        lessonRepository.save(lessonModel);
        var lessonResponse = new LessonResponse();
        BeanUtils.copyProperties(lessonModel, lessonResponse);
        return lessonResponse;
    }

    @Override
    public String deleteLessonById(UUID moduleId, UUID lessonId) {
        var lesson = getLessonById(moduleId, lessonId);;
        log.info("Deleting lesson with id: {}", lessonId);
        lessonRepository.delete(lesson);
        return "Lesson deleted successfully";
    }

    private LessonModel getLessonById(UUID moduleId, UUID lessonId) {
        log.info("Verifying lesson by id: {}", lessonId);
        return lessonRepository.findLessonIntoModule(moduleId, lessonId).orElseThrow(() -> {
            log.error("Lesson not found: {}", lessonId);
            return new ObjectNotFoundException("Lesson not found: " + lessonId);
        });    
    }

    private ModuleModel findModuleById(UUID id) {
        log.info("Verifying module by id: {}", id);
        return moduleRepository.findById(id).orElseThrow(() -> {
            log.error("Module not found: {}", id);
            return new ObjectNotFoundException("Module not found: " + id);
        });    
    }
}