package br.com.edielsonassis.authuser.dtos.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.authuser.dtos.view.UserView;
import br.com.edielsonassis.authuser.utils.validation.UsernameValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest implements Serializable {

    @UsernameValidator(groups = {UserView.registrationPost.class})
    @NotBlank(message = "UserName is required", groups = {UserView.registrationPost.class})
    @Size(min = 3, max = 50, message = "UserName must be between 3 and 50 characters", groups = {UserView.registrationPost.class})
    @JsonView(UserView.registrationPost.class)
    private String userName;

    @NotBlank(message = "Email is required", groups = {UserView.registrationPost.class, UserView.userSignin.class})
    @Email(message = "Invalid email", groups = {UserView.registrationPost.class})
    @JsonView({UserView.registrationPost.class, UserView.userSignin.class})
    private String email;

    @NotBlank(message = "Password is required", groups = {UserView.registrationPost.class, UserView.passwordPut.class, UserView.userSignin.class})
    @JsonView({UserView.registrationPost.class, UserView.passwordPut.class, UserView.userSignin.class})
    private String password;

    @NotBlank(message = "OldPassword is required", groups = {UserView.passwordPut.class})
    @JsonView(UserView.passwordPut.class)
    private String oldPassword;

    @NotBlank(message = "FullName is required", groups = {UserView.registrationPost.class, UserView.userPut.class})
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s']+$", message = "Only letters should be typed", groups = {UserView.registrationPost.class, UserView.userPut.class})
    @JsonView({UserView.registrationPost.class, UserView.userPut.class})
    private String fullName;

    @NotBlank(message = "Phone number is required", groups = {UserView.userPut.class})
    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?(\\d{2}\\s?)?(\\d{4,5}-?\\d{4})$", message = "Invalid phone number", groups = {UserView.userPut.class})
    @JsonView({UserView.registrationPost.class, UserView.userPut.class})
    private String phoneNumber;

    @NotBlank(message = "CPF is required", groups = {UserView.registrationPost.class})
    @CPF(message = "Invalid CPF", groups = {UserView.registrationPost.class})
    @JsonView(UserView.registrationPost.class)
    private String cpf;

    @NotBlank(message = "ImageUrl is required", groups = {UserView.imagePut.class})
    @JsonView({UserView.imagePut.class})
    private String imgUrl; 
}