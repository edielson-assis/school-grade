package br.com.edielsonassis.authuser.dtos.request;

import java.util.List;
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
	private List<String> roles;
}