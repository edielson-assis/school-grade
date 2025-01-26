package br.com.edielsonassis.course.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.course.dtos.request.CourseRequest;
import br.com.edielsonassis.course.dtos.response.CourseResponse;
import br.com.edielsonassis.course.dtos.views.CourseView;
import br.com.edielsonassis.course.services.CourseService;
import br.com.edielsonassis.course.specifications.SpecificationTemplete;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {
    
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> registerCourse(
            @RequestBody @Validated(CourseView.registrationPost.class) 
            @JsonView(CourseView.registrationPost.class) CourseRequest courseDto) {
        var course = courseService.saveCourse(courseDto);
        course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(
            SpecificationTemplete.CourseSpecification spec,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "10") Integer size, 
			@RequestParam(defaultValue = "asc") String direction) {
        var courses = courseService.findAllCourses(page, size, direction, spec);
        courses.stream().forEach(course -> course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel()));
        courses.forEach(course -> course.add(linkTo(methodOn(CourseController.class).getAllCourses(spec, page, size, direction)).withRel("courses")));
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getOneCourse(@PathVariable UUID courseId) {
        var course = courseService.findCourseById(courseId);
        course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable UUID courseId,
            @RequestBody @Validated(CourseView.coursePut.class) 
            @JsonView(CourseView.coursePut.class) CourseRequest courseDto) {
        var course = courseService.updateCourseById(courseId, courseDto);
        course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
        return new ResponseEntity<>(course, HttpStatus.OK);
    }   

    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable UUID courseId) {
        var course = courseService.deleteCourseById(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}