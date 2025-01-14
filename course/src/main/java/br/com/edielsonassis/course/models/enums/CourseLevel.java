package br.com.edielsonassis.course.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseLevel {
    
    BEGINNER("Beginner"),
	INTERMEDIARY("Intermediary"),
	ADVANCED("Advanced");

	private String level;
}