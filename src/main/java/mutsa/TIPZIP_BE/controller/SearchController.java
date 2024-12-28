package mutsa.TIPZIP_BE.controller;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping
    public ResponseEntity<?> searchPosts(
            @RequestParam(value = "search", required = false) String searchKeyword,
            @RequestParam(value = "tags",required = false) String tags,
            @RequestParam(defaultValue = "recent") String sort) {
        List<String> tagList = (tags != null && !tags.isEmpty())
                ? Arrays.asList(tags.split(","))
                : null;
        //검색 결과 가져오기
        try{
            // Request Body에서 태그 리스트 추출
            //List<String> tags = (body != null) ? body.get("tags") : null;
            //List<PostResponseDTO> results=searchService.searchPosts(searchKeyword,tags);
            List<PostResponseDTO> results = searchService.searchPosts(searchKeyword, tagList,sort);
            return ResponseEntity.ok(results);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }

    }
}
