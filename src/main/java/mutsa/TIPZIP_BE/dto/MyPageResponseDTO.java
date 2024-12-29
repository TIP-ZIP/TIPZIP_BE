package mutsa.TIPZIP_BE.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mutsa.TIPZIP_BE.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MyPageResponseDTO {
    private String profile_image;
    private String username;
    //private int postCount;
    private int followingCount;
    private int followerCount;
    private String message;
    private boolean badge;
    private String social_provider; // 소셜 로그인 제공자
    private String email;
    private Long user_id;

    public static MyPageResponseDTO fromMemberEntity(MemberEntity memberEntity, int followerCount, int followingCount) {
        MyPageResponseDTO myPageResponseDTO = new MyPageResponseDTO();
        myPageResponseDTO.setProfile_image(memberEntity.getProfile_image());
        myPageResponseDTO.setUsername(memberEntity.getUsername());
        myPageResponseDTO.setMessage(memberEntity.getMessage());
        myPageResponseDTO.setBadge(memberEntity.getBadge());
        //myPageResponseDTO.setPostCount(postCount);
        myPageResponseDTO.setFollowerCount(followerCount);
        myPageResponseDTO.setFollowingCount(followingCount);
        myPageResponseDTO.setSocial_provider(memberEntity.getSocial_provider().toString()); // 소셜 제공자 설정
        myPageResponseDTO.setEmail(memberEntity.getEmail()); // 이메일 설정
        myPageResponseDTO.setUser_id(memberEntity.getUserId());
        return myPageResponseDTO;
    }

}
