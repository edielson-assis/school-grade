package br.com.edielsonassis.course.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;

import br.com.edielsonassis.course.dtos.request.CourseRequest;
import br.com.edielsonassis.course.models.CourseModel;
import br.com.edielsonassis.course.models.enums.CourseLevel;
import br.com.edielsonassis.course.models.enums.CourseStatus;

public class CourseMapper {

    private static final String ZONE_ID = "America/Sao_Paulo";
    
    private CourseMapper() {}
    
    public static CourseModel toEntity(CourseRequest courseRequest) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseRequest, courseModel);
        courseModel.setCourseStatus(CourseStatus.valueOf(courseRequest.getCourseStatus().toUpperCase()));
        courseModel.setCourseLevel(CourseLevel.valueOf(courseRequest.getCourseLevel().toUpperCase()));
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
		courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return courseModel;
    }

    public static CourseModel toEntity(CourseModel courseModel, CourseRequest courseRequest) {
        courseModel.setName(courseRequest.getName());
        courseModel.setDescription(courseRequest.getDescription());
        courseModel.setImageUrl(courseRequest.getImageUrl());
        courseModel.setCourseStatus(CourseStatus.valueOf(courseRequest.getCourseStatus().toUpperCase()));
        courseModel.setCourseLevel(CourseLevel.valueOf(courseRequest.getCourseLevel().toUpperCase()));
		courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return courseModel;
    }
}