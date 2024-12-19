package mutsa.TIPZIP_BE.controller;

import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class MemberTestController {
    private final MemberService memberService;

    @Autowired
    public MemberTestController(MemberService memberService) {
        this.memberService = memberService;
    }


//    @GetMapping("/get-user")
//    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
//        try {
//            MemberDTO memberDTO = memberService.getUserFromToken(token);
//            System.out.println("MemberDTO hashCode in Controller: " + System.identityHashCode(memberDTO));
//
//            System.out.println("Returned MemberDTO: " + memberDTO);
//            return ResponseEntity.ok(memberDTO);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
//        }
//    }
}
