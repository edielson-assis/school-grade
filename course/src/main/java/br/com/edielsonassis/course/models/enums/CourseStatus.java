package br.com.edielsonassis.course.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseStatus {
    
    IN_PROGRESS("In progress"),
	CONCLUDED("Concluded");

    private String status;
}