package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mutsa.TIPZIP_BE.dto.MemberDTO;

//import jakarta.persistence.*;

@Entity // JPA Entity로 선언
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberEntity {
    @Id // 기본키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정 (필요에 따라 변경 가능)
    private Long user_id;

    @Column
    private String username;

    @Column
    private String profile_image;

    @Column
    private Boolean badge=false; //default를 false로 설정

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private OAuthProvider social_provider;

    @Column
    private String social_id;

    @Column(unique = true) // unique 제약 조건을 예시로 설정
    private String email;

    @Column
    private String message;


    // 소셜 회원용 생성 메서드 (비밀번호 없음)
    public static MemberEntity createSocialMember(MemberDTO socialMemberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUser_id(socialMemberDTO.getUser_id());
        memberEntity.setEmail(socialMemberDTO.getEmail()); // 소셜에서 제공된 이메일
        memberEntity.setSocial_id(socialMemberDTO.getSocial_id()); // 소셜에서 제공된 이름 또는 ID
        memberEntity.setUsername(null); //최초 로그인시 닉네임입력 필요하므로 null로 설정
        memberEntity.setSocial_provider(socialMemberDTO.getOAuthProvider());
        return memberEntity;
    }
}
