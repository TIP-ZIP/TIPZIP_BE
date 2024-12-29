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
public class UserPageResponseDTO {
    private String profile_image;
    private String username;
    //private int postCount;
    private int followingCount;
    private int followerCount;
    private String message;
    private boolean badge;
    private boolean following;
    private String social_provider; // 소셜 로그인 제공자
    private String email;     // 추가된 필드
    private Long user_id;

    public static UserPageResponseDTO fromMemberEntity(MemberEntity memberEntity, int followerCount, int followingCount,boolean following) {
        UserPageResponseDTO userPageResponseDTO = new UserPageResponseDTO();
        userPageResponseDTO.setProfile_image(memberEntity.getProfile_image());
        userPageResponseDTO.setUsername(memberEntity.getUsername());
        userPageResponseDTO.setMessage(memberEntity.getMessage());
        userPageResponseDTO.setBadge(memberEntity.getBadge());
        //myPageResponseDTO.setPostCount(postCount);
        userPageResponseDTO.setFollowerCount(followerCount);
        userPageResponseDTO.setFollowingCount(followingCount);
        userPageResponseDTO.setSocial_provider(memberEntity.getSocial_provider().toString()); // 소셜 제공자 설정
        userPageResponseDTO.setEmail(memberEntity.getEmail()); // 이메일 설정
        userPageResponseDTO.setFollowing(following);
        userPageResponseDTO.setUser_id(memberEntity.getUserId());
        return userPageResponseDTO;
    }

}
