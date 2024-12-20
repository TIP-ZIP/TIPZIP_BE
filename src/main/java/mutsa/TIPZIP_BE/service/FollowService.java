package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.entity.Follow;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.repository.FollowRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    public void follow(MemberEntity follower, MemberEntity following) {
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalArgumentException("이미 팔로우 중입니다.");
        }
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }
    public int getFollowingCount(MemberEntity member){
        return followRepository.countByFollower(member);
    }
    public int getFollowerCount(MemberEntity member){
        //현재 사용사를 팔로우하고 있는 유저 수 반환
        return followRepository.countByFollowing(member);
    }
}
