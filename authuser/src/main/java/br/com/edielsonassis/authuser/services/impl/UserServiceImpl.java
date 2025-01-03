package br.com.edielsonassis.authuser.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.edielsonassis.authuser.dtos.UserDto;
import br.com.edielsonassis.authuser.models.UserModel;
import br.com.edielsonassis.authuser.models.enums.UserStatus;
import br.com.edielsonassis.authuser.models.enums.UserType;
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
    private static final String ZONE_ID = "America/Sao_Paulo";

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        var userModel = convertDtoToModel(userDto);
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        log.info("Registering a new User: {}", userModel.getUserName());
        userRepository.save(userModel);
        BeanUtils.copyProperties(userModel, userDto);
        return userDto;
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("Listing all users");
        return userRepository.findAll().stream().map(UserModel -> {
            var userDto = new UserDto();
            BeanUtils.copyProperties(UserModel, userDto);
            return userDto;
        }).toList();
    }

    @Override
    public UserDto findUserById(UUID userId) {
        var userModel = findById(userId);
        var userDto = new UserDto();
        BeanUtils.copyProperties(userModel, userDto);
        return userDto; 
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
    public UserDto updateUserById(UUID userId, UserDto userDto) {
        var userModel = findById(userId);
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        log.info("Updating user with id: {}", userId);
        userRepository.save(userModel);
        BeanUtils.copyProperties(userModel, userDto);
        return userDto;
    }

    @Override
    public String updateUserPasswordById(UUID userId, UserDto userDto) {
        var userModel = findById(userId);
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        log.info("Updating password");
        userRepository.save(userModel);
        return "Password updated successfully";
    }

    @Override
    public UserDto updateUserImageById(UUID userId, UserDto userDto) {
        var userModel = findById(userId);
        userModel.setImgUrl(userDto.getImgUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        log.info("Updating image");
        userRepository.save(userModel);
        BeanUtils.copyProperties(userModel, userDto);
        return userDto;
    }

    private UserModel findById(UUID userId) {
        log.info("Verifying the user's Id: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User id not found: {}", userId);
            return new ObjectNotFoundException("User id not found: " + userId);
        }); 
    }

    private UserModel convertDtoToModel(UserDto userDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return userModel;
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
}