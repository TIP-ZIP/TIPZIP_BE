package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.ScrapDTO.ScrapRequestsDTO;
import mutsa.TIPZIP_BE.dto.ScrapDTO.ScrapResponseDTO;
import mutsa.TIPZIP_BE.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;

    // POST
//    @PostMapping
//    public ResponseEntity<?> addTag(@RequestBody ScrapRequestsDTO scrapRequestsDTO) {
//        ScrapResponseDTO scrapResponseDTO = scrapService.createScrap(token, scrapRequestsDTO);
//        return ResponseEntity.status(201).body(scrapResponseDTO);
//    }
}
