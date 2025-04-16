package pedroleonez.fornello.api.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;
import pedroleonez.fornello.api.security.userdetails.UserDetailsImpl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P";

    private static final String ISSUER = "fornello-api";

    public String generateToken(UserDetailsImpl user) {
        try {
            // defines the HMAC SHA256 algorithm to create the token signature using the defined secret key
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER) // defines the token issuer
                    .withIssuedAt(creationDate()) // sets the token issue date
                    .withExpiresAt(expirationDate()) // sets the token expiration date
                    .withSubject(user.getUsername()) // sets the token subject (in this case, the username)
                    .sign(algorithm); // signs the token using the specified algorithm
        } catch (JWTCreationException exception){
            throw new JWTCreationException("Erro ao gerar token.", exception);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            // defines the HMAC SHA256 algorithm to verify the token signature using the defined secret key
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER) // defines the token issuer
                    .build()
                    .verify(token) // verifies the token validity
                    .getSubject(); // retrieves the subject (in this case, the username) from the token
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }

    private Instant creationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(4).toInstant();
    }

}
