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
                .csrf().disable()// CSRF 비활성화
                .authorizeRequests()
                .requestMatchers("/","/auth/","/auth/login","/posts","/search","/auth/token_reissue","search").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가


        return http.build();
    }
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOriginPattern("*"); // 모든 출처 허용
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
