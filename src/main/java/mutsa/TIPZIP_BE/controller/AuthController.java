package mutsa.TIPZIP_BE.controller;

import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.dto.MemberResponseDTO;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.RefreshToken;
import mutsa.TIPZIP_BE.jwt.JwtTokenProvider;
import mutsa.TIPZIP_BE.repository.MemberRepository;
import mutsa.TIPZIP_BE.service.*;
import mutsa.TIPZIP_BE.service.AuthService.GoogleAuthService;
import mutsa.TIPZIP_BE.service.AuthService.KakaoAuthService;
import mutsa.TIPZIP_BE.service.AuthService.NaverAuthService;
import mutsa.TIPZIP_BE.service.AuthService.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final MemberRepository memberRepository;
    @Value("${spring.kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    private final MemberService memberService;
    private final KakaoAuthService kakaoAuthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleAuthService googleAuthService;
    private final MyPageService myPageService;
    private final RefreshTokenService refreshTokenService;
    private final NaverAuthService naverAuthService;
    public AuthController(MemberService memberService,
                          KakaoAuthService kakaoAuthService,
                          JwtTokenProvider jwtTokenProvider,
                          GoogleAuthService googleAuthService,
                          MemberRepository memberRepository,
                          MyPageService myPageService,
                          RefreshTokenService refreshTokenService,
                          NaverAuthService naverAuthService) {
        this.memberService=memberService;
        this.kakaoAuthService = kakaoAuthService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleAuthService = googleAuthService;
        this.memberRepository = memberRepository;
        this.myPageService = myPageService;
        this.refreshTokenService = refreshTokenService;
        this.naverAuthService = naverAuthService;
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
            case "naver":
                //네이버 인가코드를 통해 엑세스 토큰을 받아
                System.out.println("Received social provider: " + socialProvider);
                String naverAccessToken= naverAuthService.getAccessTokenFromNaver(authorizationcode);
                System.out.println("Access Token: " + naverAccessToken);
                memberDTO=memberService.getMemberFromNaver(naverAccessToken);
                break;
            case "google":
                //구글 인가코드를 통해 엑세스 토큰을 받아옴
                System.out.println("Received social provider: " + socialProvider);
                String googleAccessToken= googleAuthService.getAccessTokenFromGoogle(authorizationcode);
                System.out.println("Access Token: " + googleAccessToken);
                memberDTO=memberService.getMemberFromGoogle(googleAccessToken);
                break;
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
            //response.put("refresh_token", memberDTO.getRefreshToken());
            return ResponseEntity.status(201).body(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 성공");
            response.put("user_id", memberDTO.getSocial_id());
            response.put("username", memberDTO.getUsername());
            response.put("badge", memberDTO.getBadge());
            response.put("access_token", memberDTO.getAccessToken());
            //response.put("refresh_token", memberDTO.getRefreshToken());
            return ResponseEntity.status(200).body(response);
        }
    }
    @PostMapping("/token_reissue")
    public ResponseEntity<?> reissueAccessToken(@RequestHeader("Authorization") String token) {
        // Bearer 부분 제거
        String accessToken = token.replace("Bearer ", "");
        // 액세스 토큰의 유효성을 확인 (만료 확인)
        if (jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.badRequest().body("엑세스 토큰이 아직 유효합니다.");
        }
        // 만료된 엑세스 토큰에서 사용자 이메일 추출
        String email;
        try {
            email = jwtTokenProvider.getEmailFromExpiredToken(accessToken); // 만료된 토큰에서 이메일 추출
        } catch (Exception e) {
            return ResponseEntity.status(401).body("유효하지 않은 액세스 토큰입니다.");
        }
        // 데이터베이스에서 사용자 조회
        MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 데이터베이스에 저장된 리프레시 토큰 확인
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(email);
        if (jwtTokenProvider.validateToken(refreshToken.getToken())) {
            return ResponseEntity.status(403).body("리프레시 토큰이 유효하지 않습니다.");
        }
        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.createToken(email, 3600); // 1시간 유효
        Map<String, String> response = new HashMap<>();
        response.put("access_token", newAccessToken);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/username")
    public ResponseEntity<?> username(@RequestBody Map<String, String> request,@RequestHeader("Authorization") String token) {
        try {
            String username = request.get("username");
            if (username == null||username.isEmpty()) {
                return ResponseEntity.badRequest().body("유저네임을 입력해주세요");
            }
            myPageService.updateUsername(token, username);
            return ResponseEntity.ok("유저네임이 설정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    @GetMapping("/username/exists")
    public ResponseEntity<?> checkUsernameExists(@RequestParam String username) {
        try {
            // 유저네임 중복 검사
            myPageService.checkUsernameExists(username);
            return ResponseEntity.ok("사용 가능한 유저네임입니다.");
        } catch (IllegalArgumentException e) {
            // 중복된 경우 예외 메시지 반환
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // 기타 서버 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/badge")
    public ResponseEntity<?> addBadge(@RequestHeader("Authorization") String token) {
        MemberResponseDTO memberResponseDTO = memberService.addBadge(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try{
            String accessToken = token.replace("Bearer ", "");
            String email=jwtTokenProvider.getEmailFromToken(accessToken);
            refreshTokenService.deleteRefreshToken(email);
            return ResponseEntity.ok("로그아웃 성공");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 중 에러가 발생하였습니다.");
        }
    }
}
