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

    @Transactional
    @Override
    public UserModel saveUser(UserDto userDto) {
        var userModel = convertDtoToModel(userDto);
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        log.info("Registering a new User: {}", userModel.getUserName());
        return userRepository.save(userModel);
    }

    @Override
    public List<UserModel> getAllUsers() {
        log.info("Listing all users");
        return userRepository.findAll();
    }

    @Override
    public UserModel getOneUser(UUID userId) {
        log.info("Verifying the user's Id: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User id not found: {}", userId);
            return new ObjectNotFoundException("User id not found: " + userId);
        }); 
    }

    @Transactional
    @Override
    public void deleteUser(UUID userId) {
        var user = getOneUser(userId);
        log.info("Deleting user with username: {}", user.getUserName());
        userRepository.delete(user);
    }

    private UserModel convertDtoToModel(UserDto userDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
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