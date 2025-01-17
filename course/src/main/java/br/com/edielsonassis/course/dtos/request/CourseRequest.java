package br.com.edielsonassis.course.dtos.request;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.course.dtos.views.CourseView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequest implements Serializable {
    
	@NotBlank(message = "Name is required", groups = {CourseView.registrationPost.class, CourseView.coursePut.class})
    @JsonView({CourseView.registrationPost.class, CourseView.coursePut.class})
	private String name;

	@NotBlank(message = "Description is required", groups = {CourseView.registrationPost.class, CourseView.coursePut.class})
    @JsonView({CourseView.registrationPost.class, CourseView.coursePut.class})
	private String description;

	@NotBlank(message = "ImageUrl is required", groups = {CourseView.imagePut.class})
	@JsonView(CourseView.imagePut.class)
	private String imageUrl;
	
	@NotBlank(message = "CourseStatus is required", groups = {CourseView.registrationPost.class, CourseView.coursePut.class})
    @JsonView({CourseView.registrationPost.class, CourseView.coursePut.class})
	private String courseStatus;

	@NotBlank(message = "CourseLevel is required", groups = {CourseView.registrationPost.class, CourseView.coursePut.class})
    @JsonView({CourseView.registrationPost.class, CourseView.coursePut.class})
	private String courseLevel;

	@NotNull(message = "UserInstructor is required", groups = {CourseView.registrationPost.class})
    @JsonView(CourseView.registrationPost.class)
	private UUID userInstructor;
}