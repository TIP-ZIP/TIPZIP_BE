package mutsa.TIPZIP_BE.service;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
public class MemberService {
    // jpa, mysql dependency 추가
    private final MemberRepository memberRepository;
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";

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
        return memberDTO;
    }
}
