package br.com.edielsonassis.authuser.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    
	ROLE_ADMIN("Role_Admin"),
	ROLE_INSTRUCTOR("Role_Instructor"),
    ROLE_STUDENT("Role_Student");

    private String name;
}