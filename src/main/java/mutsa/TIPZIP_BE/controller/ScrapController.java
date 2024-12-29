package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderRequestsDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.MyPostDTO;
import mutsa.TIPZIP_BE.dto.ScrapDTO.ScrapRequestsDTO;
import mutsa.TIPZIP_BE.dto.ScrapDTO.ScrapResponseDTO;
import mutsa.TIPZIP_BE.service.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
public class ScrapController {
    private final ScrapService scrapService;

    // POST
    @PostMapping
    public ResponseEntity<?> createScrap(@RequestHeader(value = "Authorization") String token, @RequestBody ScrapRequestsDTO scrapRequestsDTO) {
        ScrapResponseDTO scrapResponseDTO = scrapService.createScrap(token, scrapRequestsDTO);
        return ResponseEntity.status(201).body(scrapResponseDTO);
    }

    // GET
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id, @RequestHeader(value = "Authorization") String token) {
        List<MyPostDTO> scrapByCategoryDTOs = scrapService.getScrapByCategory(id, token);
        return ResponseEntity.status(201).body(scrapByCategoryDTOs);
    }

    // body를 받기 위해 post로 변경
    @PostMapping("/folder")
    public ResponseEntity<?> getFolder(@RequestHeader(value = "Authorization") String token, @RequestBody Map<String, String> folderNameMap) {
        List<MyPostDTO> scrapByCategoryDTOs = scrapService.getScrapByFolder(folderNameMap.get("folder_name"), token);
        return ResponseEntity.status(201).body(scrapByCategoryDTOs);
    }

    // DELETE
    @DeleteMapping
    public ResponseEntity<?> deleteFolder(@RequestHeader(value = "Authorization") String token, @RequestBody ScrapRequestsDTO scrapRequestsDTO) {
        scrapService.deleteScrap(token, scrapRequestsDTO);
        return ResponseEntity.status(200).build();
    }

}
