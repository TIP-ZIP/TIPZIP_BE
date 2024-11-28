package mutsa.TIPZIP_BE.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
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
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            System.out.println("토큰이 유효합니다");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("토큰이 유효하지 않습니다. "+ e.getMessage());
            return false;
        }

    }
    public String getEmailFromToken(String token) {
        try {
            String email = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            System.out.println("토큰으로 추출한 유저의 이메일: " + email);
            return email;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("토큰에서 유저의 이메일 추출을 실패하였습니다: " + e.getMessage());
            return null; // 또는 예외를 던질 수 있습니다.
        }
    }

    public Authentication getAuthentication(String token) {
        String email = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return new UsernamePasswordAuthenticationToken(
                email,
                "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }




}
