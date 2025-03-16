package br.com.edielsonassis.authuser.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.authuser.dtos.response.TokenResponse;
import br.com.edielsonassis.authuser.dtos.response.UserResponse;
import br.com.edielsonassis.authuser.dtos.view.UserView;
import br.com.edielsonassis.authuser.services.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @RequestBody @Validated(UserView.registrationPost.class) 
            @JsonView(UserView.registrationPost.class) UserRequest userRequest) {
        var user = userService.saveUser(userRequest);
        user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

	@PostMapping(path = "/signin")	
    public ResponseEntity<TokenAndRefreshTokenResponse> signin(
            @RequestBody @Validated(UserView.userSignin.class) 
            @JsonView(UserView.userSignin.class) UserRequest userRequest) {
		var token = userService.signin(userRequest);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	@GetMapping(path = "/refresh/{email}")
	public ResponseEntity<TokenResponse> refreshToken(
            @PathVariable("email") String email, 
            @RequestHeader("Authorization") String refreshToken) {
        var token = userService.refreshToken(email, refreshToken);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}
}