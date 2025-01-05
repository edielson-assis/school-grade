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

import br.com.edielsonassis.authuser.dtos.PageUserDto;
import br.com.edielsonassis.authuser.dtos.UserDto;
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
    public UserDto saveUser(UserDto userDto) {
        var userModel = UserMapper.convertDtoToModel(userDto);
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        log.info("Registering a new User: {}", userModel.getUserName());
        userRepository.save(userModel);
        BeanUtils.copyProperties(userModel, userDto);
        return userDto;
    }

    @Override
    public Page<PageUserDto> findAllUsers(Integer page, Integer size, String direction, Specification<UserModel> spec) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "userId"));
        log.info("Listing all users");
        return userRepository.findAll(spec, pageable).map(userModel -> {
            var userDto = new PageUserDto();
            BeanUtils.copyProperties(userModel, userDto);
            return userDto;
        });
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
        userModel = UserMapper.updateUser(userModel, userDto);
        log.info("Updating user with id: {}", userId);
        userRepository.save(userModel);
        BeanUtils.copyProperties(userModel, userDto);
        return userDto;
    }

    @Override
    public String updateUserPasswordById(UUID userId, UserDto userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.updatePassword(userModel, userDto);
        log.info("Updating password");
        userRepository.save(userModel);
        return "Password updated successfully";
    }

    @Override
    public UserDto updateUserImageById(UUID userId, UserDto userDto) {
        var userModel = findById(userId);
        userModel = UserMapper.updateImage(userModel, userDto);
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