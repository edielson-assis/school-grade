package br.com.edielsonassis.authuser.controllers;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.dtos.response.UserResponse;
import br.com.edielsonassis.authuser.dtos.view.UserView;
import br.com.edielsonassis.authuser.services.UserService;
import br.com.edielsonassis.authuser.specifications.SpecificationTemplate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            SpecificationTemplate.UserSpecification spec,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "10") Integer size, 
			@RequestParam(defaultValue = "asc") String direction) {
        var users = userService.findAllUsers(page, size, direction, spec);
		users.stream().forEach(user -> user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel()));
        users.forEach(user -> user.add(linkTo(methodOn(UserController.class).getAllUsers(spec, page, size, direction)).withRel("users")));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getOneUser(@PathVariable UUID userId) {
        var user = userService.findUserById(userId);
        user.add(linkTo(methodOn(UserController.class).getOneUser(userId)).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody @Validated(UserView.userPut.class) 
            @JsonView(UserView.userPut.class) UserRequest userDto) {
        var user = userService.updateUser(userDto);
        user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(
            @RequestBody @Validated(UserView.passwordPut.class) 
            @JsonView(UserView.passwordPut.class) UserRequest userDto) {
        var user = userService.updateUserPassword(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/image")
    public ResponseEntity<UserResponse> updateImage(
            @RequestBody @Validated(UserView.imagePut.class) 
            @JsonView(UserView.imagePut.class) UserRequest userDto) {
        var user = userService.updateUserImage(userDto);
        user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

	@DeleteMapping(path = "/{email}")
	public ResponseEntity<Void> disableUser(@PathVariable(value = "email") String email) {
		userService.disableUser(email);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}