package br.com.edielsonassis.authuser.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.authuser.clients.CourseClient;
import br.com.edielsonassis.authuser.services.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class UserCourseController {

	private final CourseClient courseClient;
	private final UserService userService;
	
	@GetMapping("/users/user/courses")
	public ResponseEntity<?> getAllCoursesByUser(
			@PageableDefault(sort = "courseId") Pageable pageable,
			@RequestHeader("Authorization") String token) {
		var user = userService.findUser();
		var courses = courseClient.getAllCoursesByUser(user.getUserId(), pageable, token);		
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}
}