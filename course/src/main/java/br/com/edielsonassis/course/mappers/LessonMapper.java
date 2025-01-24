package br.com.edielsonassis.course.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;

import br.com.edielsonassis.course.dtos.request.LessonRequest;
import br.com.edielsonassis.course.models.LessonModel;
import br.com.edielsonassis.course.models.ModuleModel;

public class LessonMapper {

    private static final String ZONE_ID = "America/Sao_Paulo";
    
    private LessonMapper() {}
    
    public static LessonModel toEntity(LessonRequest lessonRequest, ModuleModel module) {
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonRequest, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        lessonModel.setModule(module);
        return lessonModel;
    }

    public static LessonModel toEntity(LessonModel lessonModel, LessonRequest lessonRequest) {
        lessonModel.setTitle(lessonRequest.getTitle());
        lessonModel.setDescription(lessonRequest.getDescription());
        return lessonModel;
    }
}