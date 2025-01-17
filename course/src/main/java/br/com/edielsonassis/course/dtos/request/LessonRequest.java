package br.com.edielsonassis.course.dtos.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.course.dtos.views.LessonView;
import br.com.edielsonassis.course.models.ModuleModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest implements Serializable {
    
	@NotBlank(message = "Title is required", groups = {LessonView.registrationPost.class, LessonView.lessonPut.class})
    @JsonView({LessonView.registrationPost.class, LessonView.lessonPut.class})
	private String title;

	@NotBlank(message = "Description is required", groups = {LessonView.registrationPost.class, LessonView.lessonPut.class})
    @JsonView({LessonView.registrationPost.class, LessonView.lessonPut.class})
	private String description;

	@NotBlank(message = "VideoUrl is required", groups = {LessonView.registrationPost.class, LessonView.lessonPut.class})
    @JsonView({LessonView.registrationPost.class, LessonView.lessonPut.class})
	private String videoUrl;
	
	@NotBlank(message = "Module is required", groups = {LessonView.registrationPost.class})
    @JsonView(LessonView.registrationPost.class)
	private ModuleModel module;
}