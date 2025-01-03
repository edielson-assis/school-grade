package br.com.edielsonassis.authuser.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @JsonView(UserView.publicGet.class)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        var users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @JsonView(UserView.publicGet.class)
    public ResponseEntity<UserDto> getOneUser(@PathVariable UUID userId) {
        var user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        var user = userService.deleteUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @JsonView(UserView.publicGet.class)
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
            @RequestBody @Validated(UserView.userPut.class) 
            @JsonView(UserView.userPut.class) UserDto userDto) {
        var user = userService.updateUserById(userId, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable UUID userId,
            @RequestBody @Validated(UserView.passwordPut.class) 
            @JsonView(UserView.passwordPut.class) UserDto userDto) {
        var user = userService.updateUserPasswordById(userId, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}/image")
    @JsonView(UserView.publicGet.class)
    public ResponseEntity<UserDto> updateImage(@PathVariable UUID userId,
            @RequestBody @Validated(UserView.imagePut.class) 
            @JsonView(UserView.imagePut.class) UserDto userDto) {
        var user = userService.updateUserImageById(userId, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}