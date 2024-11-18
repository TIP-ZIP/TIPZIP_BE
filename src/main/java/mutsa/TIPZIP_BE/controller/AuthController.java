package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.jwt.JwtTokenProvider;
import mutsa.TIPZIP_BE.service.KakaoAuthService;
import mutsa.TIPZIP_BE.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    private final JwtTokenProvider jwtTokenProvider;
    public AuthController(MemberService memberService, KakaoAuthService kakaoAuthService,JwtTokenProvider jwtTokenProvider){
        this.memberService=memberService;
        this.kakaoAuthService = kakaoAuthService;
        this.jwtTokenProvider = jwtTokenProvider;
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
                memberDTO=memberService.getMemberFromKakao(kakaoAccessToken);
                break;
            case "NAVER":
                return ResponseEntity.badRequest().body("Naver login 은 아직 구현전ㅜㅜ");
            case "GOOGLE":
                return ResponseEntity.badRequest().body("Naver login 은 아직 구현전ㅜㅜ");

            default:
                return ResponseEntity.badRequest().body("지원하지 않는 social provider 입니다.");
        }

        // 신규 회원 여부에 따른 응답
        if (memberDTO.isNewMember()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원 등록 성공");
            response.put("user_id", memberDTO.getSocial_id());
            response.put("username", memberDTO.getUsername());
            response.put("badge", memberDTO.getBadge());
            response.put("access_token", memberDTO.getAccessToken());
            response.put("refresh_token", memberDTO.getRefreshToken());
            return ResponseEntity.status(201).body(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 성공");
            response.put("user_id", memberDTO.getSocial_id());
            response.put("username", memberDTO.getUsername());
            response.put("badge", memberDTO.getBadge());
            response.put("access_token", memberDTO.getAccessToken());
            response.put("refresh_token", memberDTO.getRefreshToken());
            return ResponseEntity.status(200).body(response);
        }
    }
    @PostMapping("/token_reissue")
    public ResponseEntity<?> reissueAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("리프레시 토큰이 필요합니다.");
        }

        // 리프레시 토큰의 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("유효하지 않은 리프레시 토큰입니다.");
        }

        // 리프레시 토큰에서 사용자 정보 추출
        String memberName = jwtTokenProvider.getEmailFromToken(refreshToken);

        // 해당 사용자에 대한 새로운 액세스 토큰 발급
        String newAccessToken = jwtTokenProvider.createToken(memberName, 3600); // 1시간 유효

        Map<String, String> response = new HashMap<>();
        response.put("access_token", newAccessToken);
        return ResponseEntity.ok(response);
    }

}
