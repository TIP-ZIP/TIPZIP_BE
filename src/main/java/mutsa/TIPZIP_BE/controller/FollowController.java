package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.repository.MemberRepository;
import mutsa.TIPZIP_BE.service.FollowService;
import mutsa.TIPZIP_BE.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    @PostMapping("/{followingId}")
    public ResponseEntity<String> follow(@PathVariable Long followingId,@RequestHeader("Authorization") String token) {
        try{
            MemberEntity follower= memberRepository.findById(memberService.getUserFromToken(token).getUser_id())
                    .orElseThrow(() -> new IllegalArgumentException("팔로우를 요청한 유저를 찾을 수 없습니다."));
            MemberEntity following=memberRepository.findById(followingId)
                    .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 유저를 찾을 수 없습니다."));
            followService.follow(follower,following);
            return ResponseEntity.ok("팔로우 성공");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}


