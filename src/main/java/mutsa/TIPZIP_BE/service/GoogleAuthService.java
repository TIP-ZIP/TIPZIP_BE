package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.GoogleTokenResponseDTO;
import mutsa.TIPZIP_BE.dto.KakaoTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleAuthService {
    @Value("${spring.google.client_id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.google.redirect_uri}")
    private String GOOGLE_REDIRECT_URI;

    @Value("${spring.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    private static final String GRANT_TYPE = "authorization_code";

    //프론트에서 보낸 인가코드 이용해 엑세스 토큰 발급
    public String getAccessTokenFromGoogle(String authorizationcode) {
        WebClient webClient = WebClient.create();
        System.out.println("getAccessTokenFromGoogle진입성공");

        String uri = String.format(
                "https://oauth2.googleapis.com/token?grant_type=%s&client_id=%s&redirect_uri=%s&client_secret=%s&code=%s",
                GRANT_TYPE, GOOGLE_CLIENT_ID, GOOGLE_REDIRECT_URI,GOOGLE_CLIENT_SECRET,authorizationcode
        );
        System.out.println(uri);
        GoogleTokenResponseDTO response = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(String.format(
                        "grant_type=%s&client_id=%s&redirect_uri=%s&client_secret=%s&code=%s",
                        GRANT_TYPE, GOOGLE_CLIENT_ID, GOOGLE_REDIRECT_URI, GOOGLE_CLIENT_SECRET, authorizationcode
                ))
                .retrieve()
                .bodyToMono(GoogleTokenResponseDTO.class)
                .block();

        if(response!=null){
            log.info("Access Token!!:{}", response.getAccessToken());
            return response.getAccessToken();
        }
        throw new RuntimeException("구글에서 엑세스 토큰을 받아오는데 실패했습니다.");
    }
}


