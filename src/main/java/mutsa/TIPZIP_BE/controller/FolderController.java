package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderRequestsDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSimpleDTO;
import mutsa.TIPZIP_BE.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;

    // POST
    @PostMapping
    public ResponseEntity<?> createFolder(@RequestHeader(value = "Authorization") String token, @RequestBody FolderRequestsDTO folderRequestsDTO) {
        FolderResponseDTO folderResponseDTO = folderService.createFolder(token, folderRequestsDTO);
        return ResponseEntity.status(201).body(folderResponseDTO);
    }

    // GET
    @GetMapping
    public ResponseEntity<?> getPostsList(@RequestParam(defaultValue = "recent") String sort, @RequestParam(required = false) Long category) {
        List<PostSimpleDTO> postSimpleDTOSs= postService.getPostList(sort, category);
        return ResponseEntity.status(HttpStatus.OK).body(postSimpleDTOSs);
    }

    // PUT
//    @PutMapping("/{id}")
//    public ResponseEntity<?> modifyFolder(@PathVariable Long id) {
//        Folder folder = folderService.get
//
//
//    }
}
