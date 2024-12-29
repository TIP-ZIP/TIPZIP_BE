package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderCountResponseDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderRequestsDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSimpleDTO;
import mutsa.TIPZIP_BE.entity.Folder;
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
    public ResponseEntity<?> getPostsList(@RequestHeader(value = "Authorization") String token, @RequestParam(defaultValue = "false") boolean is_my) {
        List<FolderCountResponseDTO> folderCountResponseDTOS = folderService.getFolderList(is_my, token);
        return ResponseEntity.status(HttpStatus.OK).body(folderCountResponseDTOS);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Long id, @RequestHeader(value = "Authorization") String token, @RequestBody FolderRequestsDTO folderRequestsDTO) {
        FolderResponseDTO folderResponseDTO = folderService.updateFolder(id, token, folderRequestsDTO);
        return ResponseEntity.status(201).body(folderResponseDTO);
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
        return ResponseEntity.status(200).build();
    }
}
