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
                .csrf().disable()// CSRF 비활성화
                .authorizeRequests()
                .requestMatchers("/","/auth/","/auth/login","/posts","/search").permitAll()
                .requestMatchers("/auth/token_reissue","/auth/username","/posts/").authenticated()
                .requestMatchers(HttpMethod.GET,"/posts/**").authenticated()
                .requestMatchers(HttpMethod.PATCH,"/posts/**").authenticated()
                .requestMatchers(HttpMethod.DELETE,"/posts/**").authenticated()
                .requestMatchers("/scrap","/scarp/").authenticated()
                .requestMatchers("/folder","/folder/").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가


        return http.build();
    }
}
