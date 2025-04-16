package pedroleonez.fornello.api.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import pedroleonez.fornello.api.dtos.input.user.CreateUserDto;
import pedroleonez.fornello.api.enums.RoleName;
import pedroleonez.fornello.api.repositories.UserRepository;
import pedroleonez.fornello.api.services.UserService;

@Configuration
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    public ApplicationRunnerImpl(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "123456789";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            createAdminUser();
        } catch(Exception e) {
            throw new Exception("Erro ao criar usuário admin", e);
        }
    }

    private void createAdminUser() {
        if (userRepository.findByEmail(EMAIL).isEmpty()) {
            userService.createUser(new CreateUserDto(EMAIL, PASSWORD), RoleName.ROLE_ADMINISTRATOR);
        }
    }
}
