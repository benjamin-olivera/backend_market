package pe.com.market.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;          // clave HMAC
    @Value("${jwt.expiration-ms:86400000}") // 1 día por defecto
    private long expirationMs;

    public String generateToken(UserDetails user, List<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractUsername(String token) {
        return decode(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return decode(token).getClaim("roles").asList(String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            DecodedJWT jwt = decode(token);
            String username = jwt.getSubject();
            Date expiresAt = jwt.getExpiresAt();
            return username.equals(userDetails.getUsername())
                    && expiresAt != null
                    && expiresAt.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
    }
}