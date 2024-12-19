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
        // 1️⃣ 중복 확인
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalArgumentException("이미 팔로우 중입니다.");
        }
        // 2️⃣ 팔로우 생성
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }
}
