package br.com.edielsonassis.course.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;

import br.com.edielsonassis.course.dtos.request.ModuleRequest;
import br.com.edielsonassis.course.models.CourseModel;
import br.com.edielsonassis.course.models.ModuleModel;

public class ModuleMapper {
    
    private static final String ZONE_ID = "America/Sao_Paulo";
    
    private ModuleMapper() {}
    
    public static ModuleModel toEntity(ModuleRequest moduleRequest, CourseModel course) {
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleRequest, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
		moduleModel.setCourse(course);
        return moduleModel;
    }

    public static ModuleModel toEntity(ModuleModel moduleModel, ModuleRequest moduleRequest) {;
        moduleModel.setTitle(moduleRequest.getTitle());
		moduleModel.setDescription(moduleRequest.getDescription());
        return moduleModel;
    }
}