package pedroleonez.fornello.api.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pedroleonez.fornello.api.security.authentication.UserAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserAuthenticationFilter userAuthenticationFilter;

    public SecurityConfiguration(UserAuthenticationFilter userAuthenticationFilter) {
        this.userAuthenticationFilter = userAuthenticationFilter;
    }

    private static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";

    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/users/login",
            "/users/customers"
    };

    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_GET_STATUS = {
            "/products",
            "/products/{productId}",
            "/products/category/{categoryName}",
            "/products/search",
            "/orders",
            "/orders/{orderId}",
            "/orders/status/{statusName}"
    };

    private static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_POST_STATUS = {
            "/orders"
    };

    private static final String[] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_GET_STATUS = {
            "/users",
            "/users/{userId}"
    };

    private static final String[] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_POST_STATUS = {
            "/products",
            "/products/{productId}/variation"
    };

    private static final String[] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PUT_STATUS = {
            "/{productId}/variation/{productVariationId}"
    };

    private static final String[] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PATCH_STATUS = {
            "/products/{productId}",
            "/orders/{orderId}/status"
    };

    private static final String[] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_DELETE_STATUS = {
            "/users/{userId}",
            "/products/{productId}",
            "/products/{productId}/variation/{productVariationId}",
            "/orders/{productId}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(HttpMethod.GET, ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_GET_STATUS).authenticated()
                        .requestMatchers(HttpMethod.POST, ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_POST_STATUS).authenticated()
                        .requestMatchers(HttpMethod.GET, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_GET_STATUS).hasRole(ROLE_ADMINISTRATOR)
                        .requestMatchers(HttpMethod.POST, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_POST_STATUS).hasRole(ROLE_ADMINISTRATOR)
                        .requestMatchers(HttpMethod.PUT, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PUT_STATUS).hasRole(ROLE_ADMINISTRATOR)
                        .requestMatchers(HttpMethod.PATCH, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PATCH_STATUS).hasRole(ROLE_ADMINISTRATOR)
                        .requestMatchers(HttpMethod.DELETE, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_DELETE_STATUS).hasRole(ROLE_ADMINISTRATOR)
                        .anyRequest().denyAll()
                )
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
