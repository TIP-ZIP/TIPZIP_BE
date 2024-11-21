package mutsa.TIPZIP_BE.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.OAuthProvider;


//lombok dependency추가
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long user_id;
    private String username; // 사용자명
    private String profile_image;
    private Boolean badge=false;
    private OAuthProvider oAuthProvider; // 소셜 로그인 제공자 정보 (필요 시)
    // 카카오 응답의 "name" 필드를 "social_id"에 매핑
    @JsonProperty("nickname") //but, google,naver 도입 시 수정 필요.
    private String social_id;

    private String email;
    private String message;

    // 신규 회원 여부를 나타내는 플래그
    private boolean isNewMember;

    // jwt토큰 위해 추가된 필드
    private String accessToken;
    private String refreshToken;

    //lombok 어노테이션으로 getter,setter,생성자 ,tostring 메서드 생략 가능
    //소셜 회원용 메서드
    public static MemberDTO socialMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUser_id(memberEntity.getUser_id());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setUsername(memberEntity.getUsername());
        memberDTO.setSocial_id(memberEntity.getSocial_id());
        memberDTO.setOAuthProvider(memberEntity.getSocial_provider());
        return memberDTO;
    }
}
