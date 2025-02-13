package br.com.edielsonassis.authuser.dtos.response;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse implements Serializable {
    
    private UUID courseId;
	private String name;
	private String description;
	private String imageUrl;
	private String courseStatus;
	private String courseLevel;
	private UUID userInstructor;
}