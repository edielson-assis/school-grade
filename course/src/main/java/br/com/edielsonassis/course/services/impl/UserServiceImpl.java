package br.com.edielsonassis.course.services.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.edielsonassis.course.dtos.response.UserResponse;
import br.com.edielsonassis.course.models.UserModel;
import br.com.edielsonassis.course.repositories.CourseRepository;
import br.com.edielsonassis.course.repositories.UserRepository;
import br.com.edielsonassis.course.services.UserService;
import br.com.edielsonassis.course.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    @Override
    public UserModel saveUser(UserModel userModel) {
        log.info("Registering user data: {}", userModel.getFullName());
        return userRepository.save(userModel);
    }

    @Override
    public Page<UserResponse> findAllUsers(Specification<UserModel> spec, Pageable pageable) {
        log.info("Listing all users");
        return userRepository.findAll(spec, pageable).map(userModel -> {
            var userResponse = new UserResponse();
            BeanUtils.copyProperties(userModel, userResponse);
            return userResponse;
        });
    }

    @Override
    public UserModel findUserById(UUID userId) {
        return findById(userId);
    }

    @Transactional
    @Override
    public void deleteUserById(UUID userId) {
        var user = findById(userId);
        log.info("Deleting user with id: {}", user.getUserId());
        courseRepository.deleteCourseUserByUser(user.getUserId());
        user.setEnabled(false);
		userRepository.save(user);
    }   

    private UserModel findById(UUID userId) {
        log.info("Verifying the user's Id: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User id not found: {}", userId);
            return new ObjectNotFoundException("User id not found: " + userId);
        }); 
    }
}