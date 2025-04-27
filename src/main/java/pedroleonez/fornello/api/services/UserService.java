package pedroleonez.fornello.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pedroleonez.fornello.api.dtos.input.user.CreateUserDto;
import pedroleonez.fornello.api.dtos.input.user.LoginUserDto;
import pedroleonez.fornello.api.dtos.output.auth.RecoveryJwtTokenDto;
import pedroleonez.fornello.api.dtos.output.user.RecoveryUserDto;
import pedroleonez.fornello.api.entities.Role;
import pedroleonez.fornello.api.entities.User;
import pedroleonez.fornello.api.enums.RoleName;
import pedroleonez.fornello.api.exceptions.EmailAlreadyExistsException;
import pedroleonez.fornello.api.exceptions.UserAssociatedWithOrderException;
import pedroleonez.fornello.api.exceptions.UserNotFoundException;
import pedroleonez.fornello.api.mappers.UserMapper;
import pedroleonez.fornello.api.repositories.OrderRepository;
import pedroleonez.fornello.api.repositories.RoleRepository;
import pedroleonez.fornello.api.repositories.UserRepository;
import pedroleonez.fornello.api.security.authentication.JwtTokenService;
import pedroleonez.fornello.api.security.config.SecurityConfiguration;
import pedroleonez.fornello.api.security.userdetails.UserDetailsImpl;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityConfiguration securityConfiguration;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final OrderRepository orderRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, SecurityConfiguration securityConfiguration, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, OrderRepository orderRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityConfiguration = securityConfiguration;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.orderRepository = orderRepository;
        this.userMapper = userMapper;
    }

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public void createUser(CreateUserDto createUserDto, RoleName roleName) {
        if (checkIfEmailNotExists(createUserDto.email())) {

            User newUser = User.builder()
                    .email(createUserDto.email())
                    .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                    .roles(List.of(getRole(roleName)))
                    .build();

            userRepository.save(newUser);
        }
    }

    public Page<RecoveryUserDto> getUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::mapUserToUserDto);
    }

    public RecoveryUserDto getUserById(Long userId) {
        return userMapper.mapUserToUserDto(userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        if(orderRepository.findFirstByUserId(userId).isPresent()) {
            throw new UserAssociatedWithOrderException();
        }
        userRepository.deleteById(userId);
    }

    private boolean checkIfEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        return true;
    }

    private Role getRole(RoleName roleName) {
        Optional<Role> roleOptional= roleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        Role role = Role.builder().name(roleName).build();
        return roleRepository.save(role);
    }
}
