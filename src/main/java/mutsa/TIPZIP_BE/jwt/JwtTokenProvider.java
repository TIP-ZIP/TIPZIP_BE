package mutsa.TIPZIP_BE.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.token-validity-in-seconds}")
    private long validityInMilliseconds;

    //jwt서명에 사용할 SecretKey생성
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    //토큰 생성
    public String createToken(String email,long validityInMilliseconds){
        Claims claims=Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds*1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public Map<String,String> generateToken(String email){
        String accessToken=createToken(email,3600);
        String refreshToken=createToken(email,86400);
        Map<String,String> tokenMap=new HashMap<>();
        tokenMap.put("access_token",accessToken);
        tokenMap.put("refresh_token",refreshToken);
        return tokenMap;
    }
}
