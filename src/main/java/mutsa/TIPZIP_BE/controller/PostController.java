package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.PostRequestsDTO;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        Post post = postService.createPost(postRequestsDTO);
        return ResponseEntity.status(201).body(post);
    }

    // GET
    @GetMapping
    public ResponseEntity<?> getPostsList(@RequestParam(defaultValue = "recent") String sort) {
        try {
            List<Post> posts = postService.getPostList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(posts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 에러");
        }
    }

    @GetMapping("/following")
    public ResponseEntity<List<Post>> getFollowingPostsList() {

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOnePost(@PathVariable Long id) {
        try {
            Optional<Post> post = postService.getOnePost(id);
            if (post.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(post.get());
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
                return ResponseEntity.status(HttpStatus.OK).body(post.get());
            } else {
                return ResponseEntity.status(404).body("해당 게시물이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 에러");
        }
    }
}
