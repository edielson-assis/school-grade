package br.com.edielsonassis.course.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.course.dtos.response.UserResponse;
import br.com.edielsonassis.course.models.UserModel;

public interface UserService {
    
	UserModel saveUser(UserModel userModel);

    Page<UserResponse> findAllUsers(Specification<UserModel> spec, Pageable pageable);
	
	UserModel findUserById(UUID userId);

	void deleteUserById(UUID userId);
}