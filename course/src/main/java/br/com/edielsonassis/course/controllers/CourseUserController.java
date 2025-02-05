package br.com.edielsonassis.course.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.course.dtos.response.UserResponse;
import br.com.edielsonassis.course.services.CourseService;
import br.com.edielsonassis.course.services.UserService;
import br.com.edielsonassis.course.specifications.SpecificationTemplete;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CourseUserController {
	
	private final CourseService courseService;
	private final UserService userService;

	@GetMapping("/courses/{courseId}/users")
	public ResponseEntity<Page<UserResponse>> getAllUsersByCourse(
			SpecificationTemplete.UserSpecification spec,
			@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
			@PathVariable UUID courseId) {
		
		var course = courseService.findCourseById(courseId);
        var users = userService.findAllUsers(SpecificationTemplete.userCourseId(course.getCourseId()).and(spec), pageable);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
}