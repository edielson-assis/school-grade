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
public class LessonResponse extends RepresentationModel<LessonResponse>  implements Serializable {
    
    private UUID lessonId;
	private String title;
	private String description;
	private String videoUrl;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'")
	private LocalDateTime creationDate;
}