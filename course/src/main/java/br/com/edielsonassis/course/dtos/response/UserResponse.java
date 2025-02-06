package br.com.edielsonassis.course.dtos.response;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse implements Serializable {
    
    private UUID userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String cpf;
    private String imgUrl;
    private String userStatus;
    private String userType;
}