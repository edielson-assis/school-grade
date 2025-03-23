package br.com.edielsonassis.authuser.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;

import br.com.edielsonassis.authuser.dtos.request.UserEventRequest;
import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.models.RoleModel;
import br.com.edielsonassis.authuser.models.UserModel;
import br.com.edielsonassis.authuser.models.enums.UserStatus;
import br.com.edielsonassis.authuser.models.enums.UserType;

public class UserMapper {

    private static final String ZONE_ID = "America/Sao_Paulo";
    
    private UserMapper() {}

    public static UserModel toEntity(UserRequest userDto, UserType userType, RoleModel role) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserNameCustom(userDto.getUserName());
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(userType);
        userModel.getPermissions().add(role);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return userModel;
    }

    public static UserModel toEntity(UserModel userModel, UserRequest userDto) {
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return userModel;
    }

    public static void toEntity(UserModel userModel, UserType userType, RoleModel role) {
        userModel.setUserType(userType);
        userModel.getPermissions().add(role);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
    }

    public static UserModel toEntityPassword(UserModel userModel, UserRequest userDto) {
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return userModel;
    }

    public static UserModel toEntityImage(UserModel userModel, UserRequest userDto) {
        userModel.setImgUrl(userDto.getImgUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return userModel;
    }

    public static UserEventRequest toDto(UserModel userModel) {
        var userEventRequest = new UserEventRequest();
        BeanUtils.copyProperties(userModel, userEventRequest);
        userEventRequest.setUserStatus(userModel.getUserStatus().name());
        userEventRequest.setUserType(userModel.getUserType().name());
        return userEventRequest;
    }
}