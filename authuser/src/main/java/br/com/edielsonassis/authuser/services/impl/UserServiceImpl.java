package br.com.edielsonassis.authuser.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.dtos.response.UserResponse;
import br.com.edielsonassis.authuser.mappers.UserMapper;
import br.com.edielsonassis.authuser.models.RoleModel;
import br.com.edielsonassis.authuser.models.UserModel;
import br.com.edielsonassis.authuser.models.enums.ActionType;
import br.com.edielsonassis.authuser.models.enums.UserType;
import br.com.edielsonassis.authuser.publishers.UserEventPublisher;
import br.com.edielsonassis.authuser.repositories.UserRepository;
import br.com.edielsonassis.authuser.services.RoleService;
import br.com.edielsonassis.authuser.services.UserService;
import br.com.edielsonassis.authuser.services.exceptions.ObjectNotFoundException;
import br.com.edielsonassis.authuser.services.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
   
    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;
    private final RoleService roleService;
    private static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
    private static final String ROLE_STUDENT = "ROLE_STUDENT";


    @Transactional
    @Override
    public UserResponse saveUser(UserRequest userDto) {
        var userModel = UserMapper.toEntity(userDto, UserType.STUDENT, List.of(getRoleType(ROLE_STUDENT)));
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        log.info("Registering a new User: {}", userModel.getUserNameCustom());
        userRepository.save(userModel);
        publishUserEvent(userModel, ActionType.CREATE);
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Transactional
    @Override
    public UserResponse saveInstructor(UserRequest userDto) {
        var userModel = UserMapper.toEntity(userDto, UserType.INSTRUCTOR, List.of(getRoleType(ROLE_INSTRUCTOR), getRoleType(ROLE_STUDENT)));
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        log.info("Registering a new Instructor: {}", userModel.getUserNameCustom());
        userRepository.save(userModel);
        publishUserEvent(userModel, ActionType.CREATE);
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Override
    public Page<UserResponse> findAllUsers(Integer page, Integer size, String direction, Specification<UserModel> spec) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "userId"));
        log.info("Listing all users");
        return userRepository.findAll(spec, pageable).map(userModel -> {
            var userResponse = new UserResponse();
            BeanUtils.copyProperties(userModel, userResponse);
            getFormattedEnumValue(userModel, userResponse);
            return userResponse;
        });
    }

    @Override
    public UserResponse findUserById(UUID userId) {
        var userModel = findById(userId);
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Transactional
    @Override
    public String deleteUserById(UUID userId) {
        var user = findById(userId);
        log.info("Deleting user with id: {}", user.getUserId());
        userRepository.delete(user);
        publishUserEvent(user, ActionType.DELETE);
        return "User deleted successfully";
    }

    @Transactional
    @Override
    public UserResponse updateUserById(UUID userId, UserRequest userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.toEntity(userModel, userDto);
        log.info("Updating user with id: {}", userId);
        userRepository.save(userModel);
        publishUserEvent(userModel, ActionType.UPDATE);
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Transactional
    @Override
    public String updateUserPasswordById(UUID userId, UserRequest userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.toEntityPassword(userModel, userDto);
        log.info("Updating password");
        userRepository.save(userModel);
        return "Password updated successfully";
    }

    @Transactional
    @Override
    public UserResponse updateUserImageById(UUID userId, UserRequest userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.toEntityImage(userModel, userDto);
        log.info("Updating image");
        userRepository.save(userModel);
        publishUserEvent(userModel, ActionType.UPDATE);
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    private UserModel findById(UUID userId) {
        log.info("Verifying the user's Id: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User id not found: {}", userId);
            return new ObjectNotFoundException("User id not found: " + userId);
        }); 
    }

    private synchronized void validateUserNameNotExists(UserModel user) {
		log.info("Verifying the username: {}", user.getUserNameCustom());
        var exists = userRepository.existsByUserNameCustom(user.getUserNameCustom());
        if (exists) {
            log.error("Username already exists: {}", user.getUserNameCustom());
            throw new ValidationException("Username already exists");
        }
    }

    private synchronized void validateEmailNotExists(UserModel user) {
		log.info("Verifying the user's email: {}", user.getEmail());
        var exists = userRepository.existsByEmail(user.getEmail());
        if (exists) {
            log.error("Email already exists: {}", user.getEmail());
            throw new ValidationException("Email already exists");
        }
    }

    private synchronized void validateCpfNotExists(UserModel user) {
		log.info("Verifying the user's CPF: {}", user.getCpf());
        var exists = userRepository.existsByCpf(user.getCpf());
        if (exists) {
            log.error("CPF already exists: {}", user.getCpf());
            throw new ValidationException("CPF already exists");
        }
    }

    private void getFormattedEnumValue(UserModel userModel, UserResponse userResponse) {
        userResponse.setUserStatus(userModel.getUserStatus().getStatus());
        userResponse.setUserType(userModel.getUserType().getType());
    }

    private void publishUserEvent(UserModel userModel, ActionType actionType) {
        log.info("Publishing event");
        var userEventRequest = UserMapper.toDto(userModel);
        eventPublisher.publishUserEvent(userEventRequest, actionType);
    }

    private RoleModel getRoleType(String roleName) {
        return roleService.findbyRole(roleName);
    }
}