package br.com.edielsonassis.course.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.edielsonassis.course.models.ModuleModel;
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

    private final ModuleRepository moduleRepository;
	private final LessonRepository lessonRepository;

    @Override
    public void delteModule(UUID moduleId) {
        var module = findModuleById(moduleId);
        var lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
		
        if(!lessons.isEmpty() ) {
            log.info("Deleting all lessons in module with id: {}", module.getModuleId());
			lessonRepository.deleteAll(lessons);
		}
        log.info("Deleting module with id: {}", moduleId);
		moduleRepository.delete(module);
    }
    
    private ModuleModel findModuleById(UUID id) {
        log.info("Verifying module by id: {}", id);
        return moduleRepository.findById(id).orElseThrow(() -> {
            log.error("Module not found: {}", id);
            return new ObjectNotFoundException("Module not found: " + id);
        });    
    }
}