package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.jwt.JwtTokenProvider;
import mutsa.TIPZIP_BE.repository.MemberRepository;
import mutsa.TIPZIP_BE.service.FollowService;
import mutsa.TIPZIP_BE.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping("/{followingId}")
    public ResponseEntity<String> follow(@PathVariable Long followingId,@RequestHeader("Authorization") String token) {
        try{
            String followeremail=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
            MemberEntity follower_memberEntity1 = memberRepository.findByEmail(followeremail)
                    .orElseThrow(() -> new IllegalArgumentException("팔로우를 요청한 유저를 찾을 수 없습니다."));
            MemberEntity following_memberEntity2 = memberRepository.findById(followingId)
                    .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 유저를 찾을 수 없습니다."));
            //MemberEntity follower= memberRepository.findById(memberService.getUserFromToken(token).getUser_id())
            //       .orElseThrow(() -> new IllegalArgumentException("팔로우를 요청한 유저를 찾을 수 없습니다."));
            //MemberEntity following=memberRepository.findById(followingId)
            //        .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 유저를 찾을 수 없습니다."));
            followService.follow(follower_memberEntity1,following_memberEntity2);
            return ResponseEntity.ok("팔로우 성공");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    @GetMapping
    public ResponseEntity<?> getFollowInfo(@RequestHeader("Authorization")String token) {
        try{
            String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
            MemberEntity member=memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다"));
            int followingCount= followService.getFollowingCount(member);
            int followerCount= followService.getFollowerCount(member);
            System.out.println("내가 팔로우한 사람 수: " + followingCount);
            System.out.println("나를 팔로우한 사람 수: " + followerCount);
            Map<String,Integer> response=new HashMap<>();
            response.put("followingCount",followingCount);
            response.put("followerCount",followerCount);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    @DeleteMapping("/{followingId}")
    public ResponseEntity<String> unfollow(@PathVariable Long followingId,@RequestHeader("Authorization") String token) {
        try{
            String followeremail=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
            MemberEntity follower_memberEntity1 = memberRepository.findByEmail(followeremail)
                    .orElseThrow(() -> new IllegalArgumentException("팔로우를 요청한 유저를 찾을 수 없습니다."));
            MemberEntity following_memberEntity2 = memberRepository.findById(followingId)
                    .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 유저를 찾을 수 없습니다."));
            /*
            MemberEntity follower= memberRepository.findById(memberService.getUserFromToken(token).getUser_id())
                    .orElseThrow(() -> new IllegalArgumentException("팔로우 취소를 요청한 유저를 찾을 수 없습니다."));
            MemberEntity following=memberRepository.findById(followingId)
                    .orElseThrow(() -> new IllegalArgumentException("팔로우 취소대상 유저를 찾을 수 없습니다."));
             */
            followService.unfollow(follower_memberEntity1, following_memberEntity2);
            return ResponseEntity.ok("팔로우 취소 성공");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

}


