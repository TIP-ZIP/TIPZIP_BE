package mutsa.TIPZIP_BE.service;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.dto.MyPageResponseDTO;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.OAuthProvider;
import mutsa.TIPZIP_BE.jwt.JwtTokenProvider;
import mutsa.TIPZIP_BE.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
public class MemberService {
    // jpa, mysql dependency 추가
    private final MemberRepository memberRepository;
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.createSocialMember(memberDTO);
        memberRepository.save(memberEntity);
        System.out.println("멤버가 데이터베이스에 저장되었습니다.");
    }

    public MemberDTO getMemberFromKakao(String accessToken) {
        System.out.println("이제 카카오 서버에서 유저 정보를 가져오겠습니다.");
        //Webclient사용해 카카오 api 호출
        WebClient webClient = WebClient.create();
        String response = webClient.get()
                .uri(KAKAO_USERINFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // JSON 응답 파싱
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response);
        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
        JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

        // 필요한 데이터 추출
        String nickname = properties.get("nickname").getAsString();
        System.out.println("카카오에서 사용중인 닉네임: " + nickname);
        String email= kakaoAccount.get("email").getAsString();
        System.out.println("email: " + email);

        // MemberDTO 생성 및 설정
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setSocial_id(nickname); // Social_id를 MemberDTO에 설정
        memberDTO.setEmail(email);
        memberDTO.setOAuthProvider(OAuthProvider.KAKAO);

        boolean isNewMember=false;
        // 카카오로부터 받은 정보가 있을 경우 추가 설정
        if (memberDTO.getSocial_id() != null) {
            // 사용자 정보 확인을 위한 출력
            System.out.println("카카오에서 가져온 사용자 정보:");
            System.out.println("Member Name: " + memberDTO.getSocial_id());
            System.out.println("Member Email: " + memberDTO.getEmail()); // 예시로 이메일이 있다고 가정

            MemberEntity existingMember = memberRepository.findByEmail(memberDTO.getEmail()).orElse(null);
            if (existingMember == null) {
                // 신규 회원일 경우 저장
                isNewMember=true;
                System.out.println("신규회원입니다. 멤버를 저장하고싶어요..ㅠㅠ");
                save(memberDTO); //// DTO를 저장하는 메서드 호출
            }else{
                System.out.println("이미 존재하는 회원입니다.:"+existingMember.getSocial_id());
            }
        }else {
            System.out.println("카카오에서 사용자 정보를 가져오지 못했습니다.");
        }
        memberDTO.setNewMember(isNewMember);

        //JWT토큰 발급
        String jwtAccessToken=jwtTokenProvider.createToken(memberDTO.getEmail(),3600);
        String jwtRefreshToken=jwtTokenProvider.createToken(memberDTO.getEmail(),86400);
        memberDTO.setAccessToken(jwtAccessToken);
        //memberDTO.setRefreshToken(jwtRefreshToken);
        MemberEntity memberEntity = memberRepository.findByEmail(memberDTO.getEmail()).orElse(null);
        refreshTokenService.saveRefreshToken(memberEntity,jwtRefreshToken,86400);
        System.out.println("jwtAccessToken: " + jwtAccessToken);
        System.out.println("jwtRefreshToken: " + jwtRefreshToken);
        return memberDTO;

    }
    public MemberDTO getMemberFromGoogle(String accessToken) {
        System.out.println("이제 구글 서버에서 유저 정보를 가져오겠습니다.");
        //Webclient사용해 구글 api 호출
        WebClient webClient = WebClient.create();
        String response = webClient.get()
                .uri(GOOGLE_USERINFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // JSON 응답 파싱
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response);
        JsonObject googleAccount = element.getAsJsonObject();
        // 필요한 데이터 추출
        String nickname = googleAccount.get("name").getAsString();
        System.out.println("구글에서 사용중인 닉네임: " + nickname);
        String email= googleAccount.get("email").getAsString();
        System.out.println("email: " + email);

        // MemberDTO 생성 및 설정
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setSocial_id(nickname); // Social_id를 MemberDTO에 설정
        memberDTO.setEmail(email);
        memberDTO.setOAuthProvider(OAuthProvider.GOOGLE);

        boolean isNewMember=false;
        // 구글로부터 받은 정보가 있을 경우 추가 설정
        if (memberDTO.getSocial_id() != null) {
            // 사용자 정보 확인을 위한 출력
            System.out.println("구글에서 가져온 사용자 정보:");
            System.out.println("Member Name: " + memberDTO.getSocial_id());
            System.out.println("Member Email: " + memberDTO.getEmail()); // 예시로 이메일이 있다고 가정

            MemberEntity existingMember = memberRepository.findByEmail(memberDTO.getEmail()).orElse(null);
            if (existingMember == null) {
                // 신규 회원일 경우 저장
                isNewMember=true;
                System.out.println("신규회원입니다. 멤버를 저장하고싶어요..ㅠㅠ");
                save(memberDTO); //// DTO를 저장하는 메서드 호출
            }else{
                System.out.println("이미 존재하는 회원입니다.:"+existingMember.getSocial_id());
            }
        }else {
            System.out.println("구글에서 사용자 정보를 가져오지 못했습니다.");
        }
        memberDTO.setNewMember(isNewMember);

        //JWT토큰 발급
        String jwtAccessToken=jwtTokenProvider.createToken(memberDTO.getEmail(),3600);
        String jwtRefreshToken=jwtTokenProvider.createToken(memberDTO.getEmail(),86400);
        memberDTO.setAccessToken(jwtAccessToken);
        //memberDTO.setRefreshToken(jwtRefreshToken);
        MemberEntity memberEntity = memberRepository.findByEmail(memberDTO.getEmail()).orElse(null);
        refreshTokenService.saveRefreshToken(memberEntity,jwtRefreshToken,86400);
        System.out.println("jwtAccessToken: " + jwtAccessToken);
        System.out.println("jwtRefreshToken: " + jwtRefreshToken);
        return memberDTO;

    }
    public MemberDTO findByEmail(String Email ){
        MemberEntity memberEntity=memberRepository.findByEmail(Email).orElse(null);
        if (memberEntity!=null){
            return MemberDTO.socialMemberDTO(memberEntity);
        }
        return null;
    }

    public MemberDTO getUserFromToken(String token) {
        //Bearer부분 제거
        String accessToken = token.replace("Bearer ", "");

        //토큰 유효성 검사
        boolean istoken = jwtTokenProvider.validateToken(accessToken);
        if (!istoken) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        //jwt에서 이메일 추출
        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        if (email == null) {
            throw new IllegalArgumentException("토큰에서 이메일을 추출할 수 없습니다");
        }
        //이메일 통해 DB에서 유저 정보 조회
        MemberEntity memberEntity = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다"));
        MemberDTO memberDTO = MemberDTO.socialMemberDTO(memberEntity);
        System.out.println("Service 반환 전 MemberDTO: " + memberDTO); // 디버깅
        //memberentity를 MemberDTO로 변환하여 반환
        return memberDTO;
    }

    public MyPageResponseDTO getMyPage(String token){
        String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //아래는 예시이며, post, follow관련 로직 구현 후 수정필요.
        //int postCount = postRepository.countByUserId(memberEntity.getUser_id()); // 게시글 수
        //int followerCount = followerRepository.countFollowers(memberEntity.getUser_id()); // 팔로워 수
        //int followingCount = followerRepository.countFollowing(memberEntity.getUser_id()); // 팔로잉 수

        MyPageResponseDTO myPageResponseDTO = MyPageResponseDTO.fromMemberEntity(memberEntity);

        //MyPageResponseDTO myPageResponseDTO = MyPageResponseDTO.fromMemberEntity(memberEntity, postCount, followerCount, followingCount);
        return myPageResponseDTO;
    }
    public void updateUsername(String token, String newUsername){
        String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        memberEntity.setUsername(newUsername);
        memberRepository.save(memberEntity);
    }
    public void updateMessage(String token, String newMessage){
        String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        MemberEntity memberEntity =memberRepository.findByEmail(email)
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        memberEntity.setMessage(newMessage);
        memberRepository.save(memberEntity);
    }
    public MyPageResponseDTO getOtherUserPage(Long userId){
        MemberEntity memberEntity=memberRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        //아래는 예시이며, post, follow관련 로직 구현 후 수정필요.
        //int postCount = postRepository.countByUserId(memberEntity.getUser_id()); // 게시글 수
        //int followerCount = followerRepository.countFollowers(memberEntity.getUser_id()); // 팔로워 수
        //int followingCount = followerRepository.countFollowing(memberEntity.getUser_id()); // 팔로잉 수
        return MyPageResponseDTO.fromMemberEntity(memberEntity);
    }
}
