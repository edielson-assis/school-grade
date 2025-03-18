package br.com.edielsonassis.course.dtos.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEventRequest {
    
    private UUID userId;
	private String email;
	private String fullName;
	private String userStatus;
	private String userType;
	private String phoneNumber;
	private String cpf;
	private String imgUrl;
	private String actionType;
	private String role;
}