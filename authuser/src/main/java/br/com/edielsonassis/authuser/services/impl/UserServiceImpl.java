package br.com.edielsonassis.authuser.services.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.edielsonassis.authuser.dtos.UserReponse;
import br.com.edielsonassis.authuser.dtos.UserRequest;
import br.com.edielsonassis.authuser.mappers.UserMapper;
import br.com.edielsonassis.authuser.models.UserModel;
import br.com.edielsonassis.authuser.repositories.UserRepository;
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

    @Transactional
    @Override
    public UserReponse saveUser(UserRequest userDto) {
        var userModel = UserMapper.convertDtoToModel(userDto);
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        log.info("Registering a new User: {}", userModel.getUserName());
        userRepository.save(userModel);
        var userResponse = new UserReponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Override
    public Page<UserReponse> findAllUsers(Integer page, Integer size, String direction, Specification<UserModel> spec) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "userId"));
        log.info("Listing all users");
        return userRepository.findAll(spec, pageable).map(userModel -> {
            var userResponse = new UserReponse();
            BeanUtils.copyProperties(userModel, userResponse);
            getFormattedEnumValue(userModel, userResponse);
            return userResponse;
        });
    }

    @Override
    public UserReponse findUserById(UUID userId) {
        var userModel = findById(userId);
        var userResponse = new UserReponse();
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
        return "User deleted successfully";
    }

    @Transactional
    @Override
    public UserReponse updateUserById(UUID userId, UserRequest userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.updateUser(userModel, userDto);
        log.info("Updating user with id: {}", userId);
        userRepository.save(userModel);
        var userResponse = new UserReponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Transactional
    @Override
    public String updateUserPasswordById(UUID userId, UserRequest userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.updatePassword(userModel, userDto);
        log.info("Updating password");
        userRepository.save(userModel);
        return "Password updated successfully";
    }

    @Transactional
    @Override
    public UserReponse updateUserImageById(UUID userId, UserRequest userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.updateImage(userModel, userDto);
        log.info("Updating image");
        userRepository.save(userModel);
        var userResponse = new UserReponse();
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
		log.info("Verifying the username: {}", user.getUserName());
        var exists = userRepository.existsByUserName(user.getUserName());
        if (exists) {
            log.error("Username already exists: {}", user.getUserName());
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

    private void getFormattedEnumValue(UserModel userModel, UserReponse userResponse) {
        userResponse.setUserStatus(userModel.getUserStatus().getStatus());
        userResponse.setUserType(userModel.getUserType().getType());
    }
}