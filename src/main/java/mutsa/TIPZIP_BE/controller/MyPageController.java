package mutsa.TIPZIP_BE.controller;

import mutsa.TIPZIP_BE.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    private final MemberService memberService;

    public MyPageController(MemberService memberService) {
        this.memberService=memberService;

    }

    @PatchMapping("/username")
    public ResponseEntity<?> view_username(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            String newUsername = request.get("username");
            memberService.updateUsername(token, newUsername);
            return ResponseEntity.ok("유저네임이 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }

    }
    @PatchMapping("/message")
    public ResponseEntity<?> view_message(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try{
            String newMessage = request.get("message");
            memberService.updateMessage(token,newMessage);
            return ResponseEntity.ok("자기소개가 수정되었습니다.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}
