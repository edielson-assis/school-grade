package br.com.edielsonassis.authuser.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.authuser.dtos.UserDto;
import br.com.edielsonassis.authuser.dtos.view.UserView;
import br.com.edielsonassis.authuser.services.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    @JsonView(UserView.publicGet.class)
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Validated(UserView.registrationPost.class) 
            @JsonView(UserView.registrationPost.class) UserDto userDto) {
        var user = userService.saveUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}