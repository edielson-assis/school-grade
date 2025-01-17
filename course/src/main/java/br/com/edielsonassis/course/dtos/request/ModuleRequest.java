package br.com.edielsonassis.course.dtos.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.course.dtos.views.ModuleView;
import br.com.edielsonassis.course.models.CourseModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleRequest implements Serializable {
    
	@NotBlank(message = "Title is required", groups = {ModuleView.registrationPost.class, ModuleView.modulePut.class})
    @JsonView({ModuleView.registrationPost.class, ModuleView.modulePut.class})
	private String title;

	@NotBlank(message = "Description is required", groups = {ModuleView.registrationPost.class, ModuleView.modulePut.class})
    @JsonView({ModuleView.registrationPost.class, ModuleView.modulePut.class})
	private String description;
	
	@NotBlank(message = "Course is required", groups = {ModuleView.registrationPost.class})
    @JsonView(ModuleView.registrationPost.class)
	private CourseModel course;
}