package br.com.edielsonassis.authuser.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.edielsonassis.authuser.configs.security.JwtTokenProvider;
import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.authuser.dtos.response.TokenResponse;
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
import br.com.edielsonassis.authuser.services.exceptions.ValidationException;
import br.com.edielsonassis.authuser.utils.component.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
   
    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;
    private final RoleService roleService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    private static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
    private static final String ROLE_STUDENT = "ROLE_STUDENT";

    @Transactional
    @Override
    public UserResponse saveUser(UserRequest userDto) {
        var userModel = UserMapper.toEntity(userDto, UserType.STUDENT, getRoleType(ROLE_STUDENT));
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        encryptPassword(userModel);
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
        var userModel = UserMapper.toEntity(userDto, UserType.INSTRUCTOR, getRoleType(ROLE_INSTRUCTOR));
        validateUserNameNotExists(userModel);
        validateEmailNotExists(userModel);
        validateCpfNotExists(userModel);
        encryptPassword(userModel);
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
    public UserResponse findUser() {
        var userModel = currentUser();
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Transactional
    @Override
    public UserResponse updateUser(UserRequest userDto) {
        var userModel = currentUser();
        userModel = UserMapper.toEntity(userModel, userDto);
        log.info("Updating user with name: {}", userDto.getFullName());
        userRepository.save(userModel);
        publishUserEvent(userModel, ActionType.UPDATE);
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Transactional
    @Override
    public String updateUserPassword(UserRequest userDto) {
        var userModel = currentUser();
        if (!encoder.matches(userDto.getOldPassword(), userModel.getPassword())) {
            log.error("Old password does not match");
            throw new ValidationException("Old password does not match");
        }
        userModel = UserMapper.toEntityPassword(userModel, userDto);
        encryptPassword(userModel);
        log.info("Updating password");
        userRepository.save(userModel);
        return "Password updated successfully";
    }

    @Transactional
    @Override
    public UserResponse updateUserImage(UserRequest userDto) {
        var userModel = currentUser();
        userModel = UserMapper.toEntityImage(userModel, userDto);
        log.info("Updating image");
        userRepository.save(userModel);
        publishUserEvent(userModel, ActionType.UPDATE);
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(userModel, userResponse);
        getFormattedEnumValue(userModel, userResponse);
        return userResponse;
    }

    @Override
    public TokenAndRefreshTokenResponse signin(UserRequest userDto) {
		return authenticateUser(userDto);
	}
	
    @Override
	public TokenResponse refreshToken(String username, String refreshToken) {
        return tokenProvider.refreshToken(refreshToken, username);
	}

    @Transactional
    @Override
    public void disableUser(String email) {
		var user = currentUser();
		if (!(user.getEmail().equals(email) || hasPermissionToDisableUser(user))) {
            throw new AccessDeniedException("You do not have permission to delete users");
        }
		var savedUser = findUserByEmail(email);
		log.info("Disabling user with email: {}", email);
		savedUser.setEnabled(false);
		userRepository.save(savedUser);
        publishUserEvent(savedUser, ActionType.DELETE);
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

    private void encryptPassword(UserModel user) {
		log.info("Encrypting password");
        user.setPassword(encoder.encode(user.getPassword()));
    }

    private UserModel findUserByEmail(String email) {
        log.info("Verifying the user's email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("Username not found: {}", email);
            return new UsernameNotFoundException("Username not found: " + email);
        });    
    }

    private TokenAndRefreshTokenResponse authenticateUser(UserRequest userDto) {
		var username = userDto.getEmail();
		try {
			log.debug("Authenticating user with email: {}", username);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userDto.getPassword()));
			log.debug("Authentication successful for user: {}", username);
			var user = findUserByEmail(username);
			log.info("Generating access and refresh token for user: {}", username);
			return tokenProvider.createAccessTokenRefreshToken(user.getUsername(), user.getRoles());
		} catch (Exception e) {
			log.error("Invalid username or password for user: {}", username);
			throw new BadCredentialsException("Invalid username or password");
		}
	}

    private boolean hasPermissionToDisableUser(UserModel user) {
        return user.getAuthorities().stream().anyMatch(permission -> 
                permission.getAuthority().equals("ROLE_ADMIN") ||
                permission.getAuthority().equals("ROLE_MODERATOR"));
    }

    private UserModel currentUser() {
        return AuthenticatedUser.getCurrentUser();
    }
}