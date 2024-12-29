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
    private boolean following; // 추가된 필드

    public static UserPageResponseDTO fromMemberEntity(MemberEntity memberEntity, int followerCount, int followingCount,boolean following) {
        UserPageResponseDTO userPageResponseDTO = new UserPageResponseDTO();
        userPageResponseDTO.setProfile_image(memberEntity.getProfile_image());
        userPageResponseDTO.setUsername(memberEntity.getUsername());
        userPageResponseDTO.setMessage(memberEntity.getMessage());
        userPageResponseDTO.setBadge(memberEntity.getBadge());
        //myPageResponseDTO.setPostCount(postCount);
        userPageResponseDTO.setFollowerCount(followerCount);
        userPageResponseDTO.setFollowingCount(followingCount);
        userPageResponseDTO.setFollowing(following);
        return userPageResponseDTO;
    }

}
