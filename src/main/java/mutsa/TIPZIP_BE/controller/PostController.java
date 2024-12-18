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
    public ResponseEntity<?> createPost(@RequestHeader(value = "Authorization") String token, @RequestBody PostRequestsDTO postRequestsDTO) {
        PostResponseDTO postResponseDTO = postService.createPost(token, postRequestsDTO);
        return ResponseEntity.status(201).body(postResponseDTO);
    }

    // GET
    @GetMapping
    public ResponseEntity<?> getPostsList(@RequestParam(defaultValue = "recent") String sort) {
        List<PostSimpleDTO> postSimpleDTOSs= postService.getPostList();
        return ResponseEntity.status(HttpStatus.OK).body(postSimpleDTOSs);
    }

    // follow entity 생기면 추가
//    @GetMapping("/following")
//    public ResponseEntity<?> getFollowingPostsList() {
//
//    }

    // 인증된 유저
    @GetMapping("/cert")
    public ResponseEntity<?> getCertPostsList() {
        List<PostSimpleDTO> postSimpleDTOSs= postService.getPostList();
        return ResponseEntity.status(HttpStatus.OK).body(postSimpleDTOSs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOnePost(@PathVariable Long id) {
        Post post = postService.getOnePost(id);
        PostResponseDTO postResponseDTO = new PostResponseDTO(post);

        return ResponseEntity.status(200).body(postResponseDTO);
    }

    // PUT
//    @PutMapping("/{id}")
//    public ResponseEntity<?> modifyPost(@PathVariable Long id) {
//        Post post = postService.getOnePost(id);
//
//
//    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(200).build();
    }

}
