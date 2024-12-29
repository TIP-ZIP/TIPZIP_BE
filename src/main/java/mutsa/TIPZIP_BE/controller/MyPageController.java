package mutsa.TIPZIP_BE.controller;

import mutsa.TIPZIP_BE.dto.MyPageResponseDTO;
import mutsa.TIPZIP_BE.dto.UserPageResponseDTO;
import mutsa.TIPZIP_BE.jwt.JwtTokenProvider;
import mutsa.TIPZIP_BE.service.MyPageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;
    public MyPageController(MyPageService myPageService, JwtTokenProvider jwtTokenProvider) {
        this.myPageService=myPageService;
        this.jwtTokenProvider = jwtTokenProvider;

    }
    @GetMapping("/")
    public ResponseEntity<?> view_mypage(@RequestHeader("Authorization") String token) {
        try {
            MyPageResponseDTO responseDTO = myPageService.getMyPage(token);
            System.out.println("Returned responseDTO: " + responseDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    @PatchMapping("/username")
    public ResponseEntity<?> view_username(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            String newUsername = request.get("username");
            myPageService.updateUsername(token, newUsername);
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
            myPageService.updateMessage(token,newMessage);
            return ResponseEntity.ok("자기소개가 수정되었습니다.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    @PatchMapping("/profileImage")
    public ResponseEntity<?> view_profileImage(@RequestHeader("Authorization") String token, @RequestParam("file")MultipartFile file) {
        try{
            String newImageUrl=myPageService.updateProfileImage(token,file);
            Map<String,String>response=new HashMap<>();
            response.put("message","프로필 이미지가 수정되었습니다.");
            response.put("imageUrl",newImageUrl);
            return ResponseEntity.ok(response);//새롭게 업로드된 이미지 url반환
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> viewOtherUserPage(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try{
            // 현재 사용자 이메일 추출
            String email = jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
            UserPageResponseDTO responseDTO=myPageService.getOtherUserPage(id,email);
            return ResponseEntity.ok(responseDTO);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}
