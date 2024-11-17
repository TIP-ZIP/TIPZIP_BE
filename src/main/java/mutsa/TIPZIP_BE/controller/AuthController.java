package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.service.KakaoAuthService;
import mutsa.TIPZIP_BE.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Value("${spring.kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.kakao.client-secret")
    private String KAKAO_CLIENT_SECRET;
    private final MemberService memberService;
    private final KakaoAuthService kakaoAuthService;
    public AuthController(MemberService memberService, KakaoAuthService kakaoAuthService){
        this.memberService=memberService;
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleSocialLogin(@RequestBody Map<String,String> request){
        String socialProvider=request.get("social_provider");
        String authorizationcode=request.get("authorization_code");

        if (socialProvider==null || authorizationcode==null){
            return ResponseEntity.badRequest().body("social_provider 혹은 authorization code 가 null 입니다");
        }

        //소셜 로그인 로직 처리
        MemberDTO memberDTO;
        System.out.println("받은 social provider: " + socialProvider);

        switch (socialProvider.toLowerCase()){

            case "kakao":
                //카카오 인가코드를 통해 엑세스 토큰을 받아옴
                System.out.println("Received social provider: " + socialProvider);
                String kakaoAccessToken= kakaoAuthService.getAccessTokenFromKakao(authorizationcode);
                System.out.println("Access Token: " + kakaoAccessToken);
                //memberDTO=memberService.getMemberFromKakao(kakaoAccessToken);
                break;
            case "NAVER":
                return ResponseEntity.badRequest().body("Naver login 은 아직 구현전ㅜㅜ");
            case "GOOGLE":
                return ResponseEntity.badRequest().body("Naver login 은 아직 구현전ㅜㅜ");

            default:
                return ResponseEntity.badRequest().body("지원하지 않는 social provider 입니다.");
        }
        /*
        // 신규 회원 여부에 따른 응답
        if (memberDTO.isNewMember()) {
            return ResponseEntity.status(201).body(Map.of(
                    "message", "회원 등록 성공",
                    "user_id", memberDTO.getUser_id(),
                    "username", memberDTO.getUsername(),
                    "badge", memberDTO.getBadge(),
                    "access_token", memberDTO.getAccessToken(),
                    "refresh_token", memberDTO.getRefreshToken()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "message", "로그인 성공",
                    "user_id", memberDTO.getUser_id(),
                    "username", memberDTO.getUsername(),
                    "badge", memberDTO.getBadge(),
                    "access_token", memberDTO.getAccessToken(),
                    "refresh_token", memberDTO.getRefreshToken()
            ));
        }

         */
        return ResponseEntity.status(201).body("성공");
    }
}
