package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mutsa.TIPZIP_BE.dto.MemberDTO;

import java.util.ArrayList;
import java.util.List;

//import jakarta.persistence.*;

@Entity // JPA Entity로 선언
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberEntity {
    @Id // 기본키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정 (필요에 따라 변경 가능)
    @Column(name = "user_id") // DB 컬럼 이름은 그대로 user_id
    private Long userId;

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

    // 연관관계 매핑
    // folder 매핑
    @OneToMany(mappedBy = "memberEntity")
    private List<Folder> folders = new ArrayList<>();
    // scrap 매핑
    @OneToMany(mappedBy = "memberEntity")
    private List<Scrap> scraps = new ArrayList<>();
    // follow 매핑
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followingList = new ArrayList<>(); // 내가 팔로우 하는 사람들

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followerList = new ArrayList<>(); // 나를 팔로우 하는 사람들

    // 소셜 회원용 생성 메서드 (비밀번호 없음)
    public static MemberEntity createSocialMember(MemberDTO socialMemberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUserId(socialMemberDTO.getUser_id());
        memberEntity.setEmail(socialMemberDTO.getEmail()); // 소셜에서 제공된 이메일
        memberEntity.setSocial_id(socialMemberDTO.getSocial_id()); // 소셜에서 제공된 이름 또는 ID
        memberEntity.setUsername(null); //최초 로그인시 닉네임입력 필요하므로 null로 설정
        memberEntity.setSocial_provider(socialMemberDTO.getOAuthProvider());
        return memberEntity;
    }
}
