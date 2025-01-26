package br.com.edielsonassis.course.dtos.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse extends RepresentationModel<CourseResponse>  implements Serializable {
    
    private UUID courseId;
	private String name;
	private String description;
	private String imageUrl;
	private String courseStatus;
	private String courseLevel;
	private UUID userInstructor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'")
	private LocalDateTime creationDate;
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'")
	private LocalDateTime lastUpdateDate;
}