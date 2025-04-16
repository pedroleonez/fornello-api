package pedroleonez.fornello.api.security.authentication;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pedroleonez.fornello.api.entities.User;
import pedroleonez.fornello.api.exceptions.MissingTokenException;
import pedroleonez.fornello.api.exceptions.model.ApiError;
import pedroleonez.fornello.api.repositories.UserRepository;
import pedroleonez.fornello.api.security.config.SecurityConfiguration;
import pedroleonez.fornello.api.security.userdetails.UserDetailsImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public UserAuthenticationFilter(JwtTokenService jwtTokenService, UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // checks if the endpoint requires authentication before processing the request
            if (checkIfEndpointIsNotPublic(request)) {
                String token = recoveryToken(request); // recovers the token from the Authorization header of the request
                if (token != null) {
                    String subject = jwtTokenService.getSubjectFromToken(token); // gets the subject (in this case, the username) from the token
                    User user = userRepository.findByEmail(subject).get(); // searches the user by email (which is the subject of the token)
                    UserDetailsImpl userDetails = new UserDetailsImpl(user); // creates a UserDetails with the found user

                    // creates an authentication object for Spring Security
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                    // sets the authentication object in the Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new MissingTokenException();
                }
            }
            filterChain.doFilter(request, response); // continues the processing of the request
        } catch (MissingTokenException ex) {
            buidErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.name(), ex, response);
        } catch (JWTCreationException ex) {
            buidErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), ex, response);
        } catch (JWTVerificationException ex) {
            buidErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), ex, response);
        }
    }

    // recovers the token from the Authorization header of the request
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // checks if the endpoint requires authentication before processing the request
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }

    // builds and sends a JSON error response in case of exception
    private void buidErrorResponse(Integer codeError, String statusError, Exception ex, HttpServletResponse response) throws IOException {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(codeError)
                .status(statusError)
                .errors(List.of(ex.getMessage()))
                .build();
        response.setStatus(codeError);
        response.setContentType("application/json");
        response.getWriter().write(convertObjToJson(apiError));
    }

    // converts a Java object to JSON format
    private String convertObjToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

}
