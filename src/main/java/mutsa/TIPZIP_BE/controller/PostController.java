package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSimpleDTO;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // POST
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequestsDTO postRequestsDTO) {
        try {
            Post post = postService.createPost(postRequestsDTO);
            PostResponseDTO postResponseDTO = new PostResponseDTO(post);
            return ResponseEntity.status(201).body(postResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // GET
    @GetMapping
    public ResponseEntity<?> getPostsList(@RequestParam(defaultValue = "recent") String sort) {
        try {
            List<PostSimpleDTO> postSimpleDTOSs= postService.getPostList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(postSimpleDTOSs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 에러");
        }
    }

    // follow entity 생기면 추가
//    @GetMapping("/following")
//    public ResponseEntity<List<Post>> getFollowingPostsList() {
//
//    }

    // 인증된 유저
//    @GetMapping("/cert")
//    public ResponseEntity<List<Post>> getCertPostsList() {
//    
//    member repository에 findByActiveTrue() 추가하고 추가
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOnePost(@PathVariable Long id) {
        try {
            Optional<Post> post = postService.getOnePost(id);
            if (post.isPresent()) {
                PostResponseDTO postResponseDTO = new PostResponseDTO(post.get());
                return ResponseEntity.status(HttpStatus.OK).body(postResponseDTO);
            } else {
                return ResponseEntity.status(404).body("해당 게시물이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 에러");
        }
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyPost(@PathVariable Long id) {
        try {
            Optional<Post> post = postService.getOnePost(id);
            if (post.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(post);
            } else {
                return ResponseEntity.status(404).body("해당 게시물이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 에러");
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            Optional<Post> post = postService.getOnePost(id);
            if (post.isPresent()) {
                postService.deletePost(id);
                return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
            } else {
                return ResponseEntity.status(404).body("해당 게시물이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 에러");
        }
    }

}
