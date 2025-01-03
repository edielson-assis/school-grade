package br.com.edielsonassis.authuser.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.authuser.models.enums.UserStatus;
import br.com.edielsonassis.authuser.models.enums.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto implements Serializable {

    public interface UserView {

        public static interface registrationPost{}
        public static interface userPut{}
        public static interface passwordPut{}
        public static interface imagePut{}
        public static interface publicGet{} 
    }
    
    @JsonView(UserView.publicGet.class)
    private UUID userId;

    @JsonView({UserView.registrationPost.class, UserView.publicGet.class})
    private String userName;

    @JsonView({UserView.registrationPost.class, UserView.publicGet.class})
    private String email;

    @JsonView({UserView.registrationPost.class, UserView.passwordPut.class})
    private String password;

    @JsonView(UserView.passwordPut.class)
    private String oldPassword;

    @JsonView({UserView.registrationPost.class, UserView.userPut.class, UserView.publicGet.class})
    private String fullName;

    @JsonView({UserView.registrationPost.class, UserView.userPut.class, UserView.publicGet.class})
    private String phoneNumber;

    @JsonView({UserView.registrationPost.class, UserView.publicGet.class})
    private String cpf;

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