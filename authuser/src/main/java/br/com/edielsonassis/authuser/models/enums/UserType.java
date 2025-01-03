package br.com.edielsonassis.authuser.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    
    ADMIN("Admin"),
    STUDENT("Student"),
    INSTRUCTOR("Instructor");

    private String type;
}