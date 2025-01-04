package br.com.edielsonassis.authuser.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidatorImpl implements ConstraintValidator<UsernameValidator, String> {

    @Override
	public void initialize(UsernameValidator constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && username.matches("^[a-zA-Z0-9]+$");
    }
}