package br.com.edielsonassis.authuser.models;

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