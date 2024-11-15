package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.KakaoTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoAuthService {
    @Value("${spring.kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    private static final String GRANT_TYPE = "authorization_code";

    //프론트에서 보낸 인가코드 이용해 엑세스 토큰 발급
    public String getAccessTokenFromKakao(String authorizationcode) {
        WebClient webClient = WebClient.create();
        System.out.println("getAccessTokenFromKakao진입성공");

        String uri = String.format(
                "https://kauth.kakao.com/oauth/token?grant_type=%s&client_id=%s&redirect_uri=%s&code=%s",
                GRANT_TYPE, KAKAO_CLIENT_ID, KAKAO_REDIRECT_URI,authorizationcode
        );
        System.out.println(uri);

        KakaoTokenResponseDTO response=webClient.post()
                .uri(uri)
                .header("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();
        if(response!=null){
            log.info("Access Token!!:{}", response.getAccessToken());
            return response.getAccessToken();
        }
        throw new RuntimeException("카카오에서 엑세스 토큰을 받아오는데 실패했습니다.");
    }
}
