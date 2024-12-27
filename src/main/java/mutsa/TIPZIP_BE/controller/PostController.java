package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.MyPostDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSimpleDTO;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> getPostsList(@RequestParam(defaultValue = "recent") String sort, @RequestParam(required = false) Long category) {
        List<PostSimpleDTO> postSimpleDTOSs= postService.getPostList(sort, category);
        return ResponseEntity.status(HttpStatus.OK).body(postSimpleDTOSs);
    }

    // 팔로잉 유저
    @GetMapping("/following")
    public ResponseEntity<?> getFollowingPostsList(@RequestHeader(value = "Authorization") String token, @RequestParam(defaultValue = "recent") String sort, @RequestParam(required = false) Long category) {
        List<PostSimpleDTO> postSimpleDTOSs= postService.getFollowingPostsList(token, sort, category);
        return ResponseEntity.status(HttpStatus.OK).body(postSimpleDTOSs);
    }

    // 인증된 유저
    @GetMapping("/cert")
    public ResponseEntity<?> getCertPostsList(@RequestParam(defaultValue = "recent") String sort, @RequestParam(required = false) Long category) {
        List<PostSimpleDTO> postSimpleDTOSs= postService.getCertPostsList(sort, category);
        return ResponseEntity.status(HttpStatus.OK).body(postSimpleDTOSs);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getOnePost(@PathVariable Long id) {
        Post post = postService.getOnePost(id);
        PostResponseDTO postResponseDTO = new PostResponseDTO(post);

        return ResponseEntity.status(200).body(postResponseDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getMyPosts(@PathVariable Long id) {
        List<MyPostDTO> myPostDTOS = postService.getMyposts(id);
        return ResponseEntity.status(200).body(myPostDTOS);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestHeader(value = "Authorization") String token, @RequestBody PostRequestsDTO postRequestsDTO) {
        PostResponseDTO postResponseDTO = postService.updatePost(id, token, postRequestsDTO);
        return ResponseEntity.status(201).body(postResponseDTO);
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(200).build();
    }

}
