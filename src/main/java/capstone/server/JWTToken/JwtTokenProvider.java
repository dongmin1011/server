package capstone.server.JWTToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secretKey = UUID.randomUUID().toString();

    private long jwtExpirationInMs = 3600000;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String LoginId, String password) {
//        jwtSecret = JwtUtils.generateJwtSecret();

        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("id", ID);
//        claims.put("name", name);
        Claims claims = Jwts.claims().setSubject(LoginId);
        claims.put("LoginId", LoginId);
        claims.put("password", password);

        System.out.println("secretKey = " + secretKey);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

