package mutsa.TIPZIP_BE.config;

import mutsa.TIPZIP_BE.jwt.JwtAuthenticationFilter;
import mutsa.TIPZIP_BE.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) CORS 설정
                .cors(config -> config.configurationSource(corsConfigurationSource()))
                // 2) CSRF 비활성화
                .csrf(csrf -> csrf.disable())
                // 3) 요청별 권한
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/auth/", "/auth/login", "/posts", "/search", "/auth/token_reissue", "search").permitAll()
                        .requestMatchers("/auth/username","/posts/").authenticated()
                        .requestMatchers("/mypage/username").authenticated()
                        .requestMatchers(HttpMethod.GET,"/mypage/**").authenticated()
                        .requestMatchers(HttpMethod.POST,"/mypage/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/mypage/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/mypage/**").authenticated()
                        .requestMatchers(HttpMethod.GET,"/posts/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/posts/**").authenticated()
                        .requestMatchers("/follow/**").authenticated()
                        .requestMatchers("/scrap","/scarp/").authenticated()
                        .requestMatchers("/folder","/folder/").authenticated()
                        .anyRequest().permitAll()
                )
                // 4) JWT 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // 모든 Origin 허용
        config.addAllowedHeader("*");        // 모든 헤더 허용
        config.addAllowedMethod("*");        // 모든 메서드 허용
        config.setAllowCredentials(true);    // 인증정보(쿠키 등) 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

