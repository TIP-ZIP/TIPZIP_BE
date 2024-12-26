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

    public static MyPageResponseDTO fromMemberEntity(MemberEntity memberEntity, int followerCount, int followingCount) {
        MyPageResponseDTO myPageResponseDTO = new MyPageResponseDTO();
        myPageResponseDTO.setProfile_image(memberEntity.getProfile_image());
        myPageResponseDTO.setUsername(memberEntity.getUsername());
        myPageResponseDTO.setMessage(memberEntity.getMessage());
        myPageResponseDTO.setBadge(memberEntity.getBadge());
        //myPageResponseDTO.setPostCount(postCount);
        myPageResponseDTO.setFollowerCount(followerCount);
        myPageResponseDTO.setFollowingCount(followingCount);
        return myPageResponseDTO;
    }

}
