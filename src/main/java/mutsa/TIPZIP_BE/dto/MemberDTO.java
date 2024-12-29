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
    private OAuthProvider oAuthProvider;

    @JsonProperty("nickname")
    private String social_id;

    private String email;
    private String message;

    // 신규 회원 여부를 나타내는 플래그
    private boolean isNewMember;

    // jwt토큰 위해 추가된 필드
    private String accessToken;
    private String refreshToken;

    //소셜 회원용 메서드
    public static MemberDTO socialMemberDTO(MemberEntity memberEntity){
        if (memberEntity == null) {
            throw new IllegalArgumentException("MemberEntity가 null입니다.");
        }

        System.out.println("MemberEntity 값: " + memberEntity); // 디버깅용 출력
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUser_id(memberEntity.getUserId());
        System.out.println("User_id 설정: " + memberDTO.getUser_id());
        memberDTO.setEmail(memberEntity.getEmail());
        System.out.println("Email 설정: " + memberDTO.getEmail());
        memberDTO.setUsername(memberEntity.getUsername());
        System.out.println("Username설정: "+memberDTO.getUsername());
        memberDTO.setSocial_id(memberEntity.getSocial_id());
        System.out.println("Social_id 설정: " + memberDTO.getSocial_id());
        memberDTO.setOAuthProvider(memberEntity.getSocial_provider());
        System.out.println("Social_provider: " + memberDTO.getOAuthProvider());
        System.out.println("MemberDTO hashCode in Service: " + System.identityHashCode(memberDTO));

        return memberDTO;
    }
}
