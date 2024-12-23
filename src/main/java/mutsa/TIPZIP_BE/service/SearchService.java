package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final PostRepository postRepository;
    @Transactional
    public List<PostResponseDTO> searchPosts(String searchKeyword,List<String> tags){
        if(searchKeyword==null || searchKeyword.isEmpty() && (tags == null || tags.isEmpty())){
            throw new IllegalArgumentException("검색어 또는 태그를 입력해주세요");
        }

        List<Post>posts=postRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword);
        // 검색 로직
        /*
        List<Post> posts;

        if (tags != null && !tags.isEmpty() && (searchKeyword != null && !searchKeyword.isBlank())) {
            // 검색어와 태그 모두가 있는 경우
            posts = postRepository.findByTitleContainingOrContentContainingAndTagsIn(
                    searchKeyword, searchKeyword, tags
            );
        } else if (tags != null && !tags.isEmpty()) {
            // 태그만 있는 경우
            posts = postRepository.findByTagsIn(tags);
        } else {
            // 검색어만 있는 경우
            posts = postRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword);
        }

         */
        return posts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());


    }
}
