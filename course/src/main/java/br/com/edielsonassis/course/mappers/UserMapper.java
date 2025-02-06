package br.com.edielsonassis.course.mappers;

import org.springframework.beans.BeanUtils;

import br.com.edielsonassis.course.dtos.request.UserEventRequest;
import br.com.edielsonassis.course.models.UserModel;

public class UserMapper {
    
    private UserMapper() {}

    public static UserModel toDto(UserEventRequest  userEvent) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userEvent, userModel);
        return userModel;
    }
}