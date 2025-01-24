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

import br.com.edielsonassis.course.dtos.request.ModuleRequest;
import br.com.edielsonassis.course.dtos.response.ModuleResponse;
import br.com.edielsonassis.course.mappers.ModuleMapper;
import br.com.edielsonassis.course.models.CourseModel;
import br.com.edielsonassis.course.models.ModuleModel;
import br.com.edielsonassis.course.repositories.CourseRepository;
import br.com.edielsonassis.course.repositories.LessonRepository;
import br.com.edielsonassis.course.repositories.ModuleRepository;
import br.com.edielsonassis.course.services.ModuleService;
import br.com.edielsonassis.course.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class ModuleServiceImpl implements ModuleService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public ModuleResponse saveModule(UUID courseId, ModuleRequest moduleRequest) {
        var course = findCourseById(courseId);
        var moduleModel = ModuleMapper.toEntity(moduleRequest, course);
        log.info("Registering a new Module: {}", moduleModel.getTitle());
		moduleRepository.save(moduleModel);
        var moduleResponse = new ModuleResponse();
        BeanUtils.copyProperties(moduleModel, moduleResponse);
        return moduleResponse;
    }

    @Override
    public Page<ModuleResponse> findAllModules(Integer page, Integer size, String direction, Specification<ModuleModel> spec) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "moduleId"));
        log.info("Listing all modules");
        return moduleRepository.findAll(spec, pageable).map(moduleModel -> {
            var moduleResponse = new ModuleResponse();
            BeanUtils.copyProperties(moduleModel, moduleResponse);
            return moduleResponse;
        });
    }

    @Override
    public ModuleResponse findModuleById(UUID courseId, UUID moduleId) {
        var moduleModel = getModuleById(courseId, moduleId);
        var moduleResponse = new ModuleResponse();
        BeanUtils.copyProperties(moduleModel, moduleResponse);
        return moduleResponse;
    }

    @Override
    public String deleteModuleById(UUID courseId, UUID moduleId) {
        var module = getModuleById(courseId, moduleId);
        var lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
		
        if(!lessons.isEmpty() ) {
            log.info("Deleting all lessons in module with id: {}", module.getModuleId());
			lessonRepository.deleteAll(lessons);
		}
        log.info("Deleting module with id: {}", moduleId);
		moduleRepository.delete(module);
        return "Module deleted successfully";
    }

    @Transactional
    @Override
    public ModuleResponse updateModuleById(UUID courseId, UUID moduleId, ModuleRequest moduleRequest) {
        var moduleModel = getModuleById(courseId, moduleId);
        moduleModel = ModuleMapper.toEntity(moduleModel, moduleRequest);
        log.info("Updating module with id: {}", moduleId);
        moduleRepository.save(moduleModel);
        var moduleResponse = new ModuleResponse();
        BeanUtils.copyProperties(moduleModel, moduleResponse);
        return moduleResponse;
    }

    private ModuleModel getModuleById(UUID courseId, UUID moduleId) {
        log.info("Verifying module by id: {}", moduleId);
        return moduleRepository.findModuleIntoCourse(courseId, moduleId).orElseThrow(() -> {
            log.error("Module not found: {}", moduleId);
            return new ObjectNotFoundException("Module not found: " + moduleId);
        });    
    }

    private CourseModel findCourseById(UUID id) {
        log.info("Verifying course by id: {}", id);
        return courseRepository.findById(id).orElseThrow(() -> {
            log.error("Course not found: {}", id);
            return new ObjectNotFoundException("Course not found: " + id);
        });    
    }
}