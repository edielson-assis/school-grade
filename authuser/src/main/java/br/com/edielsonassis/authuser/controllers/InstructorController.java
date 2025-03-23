package br.com.edielsonassis.authuser.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.dtos.response.UserResponse;
import br.com.edielsonassis.authuser.dtos.view.UserView;
import br.com.edielsonassis.authuser.services.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/instructors")
public class InstructorController {

	private final UserService userService;

    @PutMapping("/subscription")
    public ResponseEntity<UserResponse> registerInstructor(
            @RequestBody @Validated(UserView.userInstructor.class) 
            @JsonView(UserView.userInstructor.class) UserRequest userDto) {
        var user = userService.saveInstructor(userDto);
        user.add(linkTo(methodOn(UserController.class).getOneUser()).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}