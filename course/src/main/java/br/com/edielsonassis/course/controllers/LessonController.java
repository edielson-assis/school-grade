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

import br.com.edielsonassis.course.dtos.request.LessonRequest;
import br.com.edielsonassis.course.dtos.response.LessonResponse;
import br.com.edielsonassis.course.dtos.views.LessonView;
import br.com.edielsonassis.course.services.LessonService;
import br.com.edielsonassis.course.specifications.SpecificationTemplete;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/module/{moduleId}/lesson")
public class LessonController {
    
    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonResponse> registerLesson(
            @PathVariable UUID moduleId,
            @RequestBody @Validated(LessonView.registrationPost.class) 
            @JsonView(LessonView.registrationPost.class) LessonRequest lessonDto) {
        var lesson = lessonService.saveLesson(moduleId, lessonDto);
        lesson.add(linkTo(methodOn(LessonController.class).getOneLesson(moduleId, lesson.getLessonId())).withSelfRel());
        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<LessonResponse>> getAllLessons(
            @PathVariable UUID moduleId,
            SpecificationTemplete.LessonSpecification spec,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "10") Integer size, 
			@RequestParam(defaultValue = "asc") String direction) {
        var lessons = lessonService.findAllLessons(page, size, direction, SpecificationTemplete.lessonModuleId(moduleId).and(spec));
        lessons.stream().forEach(lesson -> lesson.add(linkTo(methodOn(LessonController.class).getOneLesson(moduleId, lesson.getLessonId())).withSelfRel()));
        lessons.forEach(lesson -> lesson.add(linkTo(methodOn(LessonController.class).getAllLessons(moduleId, spec, page, size, direction)).withRel("lessons")));
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        var lesson = lessonService.findLessonById(moduleId, lessonId);
        lesson.add(linkTo(methodOn(LessonController.class).getOneLesson(moduleId, lesson.getLessonId())).withSelfRel());
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable UUID moduleId,
            @PathVariable UUID lessonId,
            @RequestBody @Validated(LessonView.lessonPut.class) 
            @JsonView(LessonView.lessonPut.class) LessonRequest lessonDto) {
        var lesson = lessonService.updateLessonById(moduleId, lessonId, lessonDto);
        lesson.add(linkTo(methodOn(LessonController.class).getOneLesson(moduleId, lesson.getLessonId())).withSelfRel());
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }   

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<String> deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        var lesson = lessonService.deleteLessonById(moduleId, lessonId);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }
}