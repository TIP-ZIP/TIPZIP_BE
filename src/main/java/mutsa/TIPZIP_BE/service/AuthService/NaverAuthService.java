package mutsa.TIPZIP_BE.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.TokenResponseDTO.NaverTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverAuthService {
    @Value("${spring.naver.client_id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.naver.redirect_uri}")
    private String NAVER_REDIRECT_URI;

    private static final String GRANT_TYPE = "authorization_code";

    //프론트에서 보낸 인가코드 이용해 엑세스 토큰 발급
    public String getAccessTokenFromNaver(String authorizationcode) {
        WebClient webClient = WebClient.create();
        String state="test";//프론트에서 STATE를 고정된 값인 test로 설정함에 따라 백도 이와 같이 설정
        System.out.println("getAccessTokenFromNaver진입성공");

        String uri = String.format(
                "https://nid.naver.com/oauth2.0/token?grant_type=%s&client_id=%s&client_secret=%s&redirect_uri=%s&code=%s&state=%s",
                GRANT_TYPE, NAVER_CLIENT_ID,NAVER_CLIENT_SECRET, NAVER_REDIRECT_URI,authorizationcode,state
        );
        System.out.println(uri);

        NaverTokenResponseDTO response=webClient.post()
                .uri(uri)
                .header("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(NaverTokenResponseDTO.class)
                .block();
        if(response!=null){
            log.info("Access Token!!:{}", response.getAccessToken());
            return response.getAccessToken();
        }
        throw new RuntimeException("네이버에서 엑세스 토큰을 받아오는데 실패했습니다.");
    }
}
