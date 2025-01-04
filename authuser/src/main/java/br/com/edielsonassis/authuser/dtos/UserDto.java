package br.com.edielsonassis.authuser.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.authuser.dtos.view.UserView;
import br.com.edielsonassis.authuser.models.enums.UserStatus;
import br.com.edielsonassis.authuser.models.enums.UserType;
import br.com.edielsonassis.authuser.utils.validation.UsernameValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto implements Serializable {
    
    @JsonView(UserView.publicGet.class)
    private UUID userId;

    @UsernameValidator(groups = {UserView.registrationPost.class})
    @NotBlank(message = "UserName is required", groups = {UserView.registrationPost.class})
    @Size(min = 3, max = 50, message = "UserName must be between 3 and 50 characters", groups = {UserView.registrationPost.class})
    @JsonView({UserView.registrationPost.class, UserView.publicGet.class})
    private String userName;

    @NotBlank(message = "Email is required", groups = {UserView.registrationPost.class})
    @Email(message = "Invalid email", groups = {UserView.registrationPost.class})
    @JsonView({UserView.registrationPost.class, UserView.publicGet.class})
    private String email;

    @NotBlank(message = "Password is required", groups = {UserView.registrationPost.class, UserView.passwordPut.class})
    @JsonView({UserView.registrationPost.class, UserView.passwordPut.class})
    private String password;

    @NotBlank(message = "OldPassword is required", groups = {UserView.passwordPut.class})
    @JsonView(UserView.passwordPut.class)
    private String oldPassword;

    @NotBlank(message = "FullName is required", groups = {UserView.registrationPost.class, UserView.userPut.class})
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s']+$", message = "Only letters should be typed", groups = {UserView.registrationPost.class, UserView.userPut.class})
    @JsonView({UserView.registrationPost.class, UserView.userPut.class, UserView.publicGet.class})
    private String fullName;

    @NotBlank(message = "Phone number is required", groups = {UserView.userPut.class})
    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?(\\d{2}\\s?)?(\\d{4,5}-?\\d{4})$", message = "Invalid phone number", groups = {UserView.userPut.class})
    @JsonView({UserView.registrationPost.class, UserView.userPut.class, UserView.publicGet.class})
    private String phoneNumber;

    @NotBlank(message = "CPF is required", groups = {UserView.registrationPost.class})
    @CPF(message = "Invalid CPF", groups = {UserView.registrationPost.class})
    @JsonView({UserView.registrationPost.class, UserView.publicGet.class})
    private String cpf;

    @NotBlank(message = "ImageUrl is required", groups = {UserView.imagePut.class})
    @JsonView({UserView.imagePut.class, UserView.publicGet.class})
    private String imgUrl;

    @JsonView(UserView.publicGet.class)
    private UserStatus userStatus;

    @JsonView(UserView.publicGet.class)
    private UserType userType;
    
    @JsonView(UserView.publicGet.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JsonView(UserView.publicGet.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdateDate;    
}