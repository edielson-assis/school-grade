package br.com.edielsonassis.course.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.course.dtos.request.ModuleRequest;
import br.com.edielsonassis.course.dtos.response.ModuleResponse;
import br.com.edielsonassis.course.models.ModuleModel;

public interface ModuleService {

    ModuleResponse saveModule(UUID courseId, ModuleRequest moduleRequest);

    Page<ModuleResponse> findAllModules(Integer page, Integer size, String direction, Specification<ModuleModel> spec);
    
    ModuleResponse findModuleById(UUID courseId, UUID moduleId);
    
    String delteModuleById(UUID courseId, UUID moduleId);

    ModuleResponse updateModuleById(UUID courseId, UUID moduleId, ModuleRequest moduleRequest);
}