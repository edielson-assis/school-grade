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

import br.com.edielsonassis.course.dtos.request.ModuleRequest;
import br.com.edielsonassis.course.dtos.response.ModuleResponse;
import br.com.edielsonassis.course.dtos.views.ModuleView;
import br.com.edielsonassis.course.services.ModuleService;
import br.com.edielsonassis.course.specifications.SpecificationTemplete;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/course/{courseId}/module")
public class ModuleController {
    
    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity<ModuleResponse> registerModule(
            @PathVariable UUID courseId,
            @RequestBody @Validated(ModuleView.registrationPost.class) 
            @JsonView(ModuleView.registrationPost.class) ModuleRequest moduleDto) {
        var module = moduleService.saveModule(courseId, moduleDto);
        module.add(linkTo(methodOn(ModuleController.class).getOneModule(courseId, module.getModuleId())).withSelfRel());
        return new ResponseEntity<>(module, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ModuleResponse>> getAllModules(
            @PathVariable UUID courseId,
            SpecificationTemplete.ModuleSpecification spec,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "10") Integer size, 
			@RequestParam(defaultValue = "asc") String direction) {
        var modules = moduleService.findAllModules(page, size, direction, SpecificationTemplete.moduleCourseId(courseId).and(spec));
        modules.stream().forEach(module -> module.add(linkTo(methodOn(ModuleController.class).getOneModule(courseId, module.getModuleId())).withSelfRel()));
        modules.forEach(module -> module.add(linkTo(methodOn(ModuleController.class).getAllModules(courseId, spec, page, size, direction)).withRel("modules")));
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> getOneModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        var module = moduleService.findModuleById(courseId, moduleId);
        module.add(linkTo(methodOn(ModuleController.class).getOneModule(courseId, module.getModuleId())).withSelfRel());
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> updateModule(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId,
            @RequestBody @Validated(ModuleView.modulePut.class) 
            @JsonView(ModuleView.modulePut.class) ModuleRequest moduleDto) {
        var module = moduleService.updateModuleById(courseId, moduleId, moduleDto);
        module.add(linkTo(methodOn(ModuleController.class).getOneModule(courseId, module.getModuleId())).withSelfRel());
        return new ResponseEntity<>(module, HttpStatus.OK);
    }   

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<String> deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        var module = moduleService.deleteModuleById(courseId, moduleId);
        return new ResponseEntity<>(module, HttpStatus.OK);
    }
}