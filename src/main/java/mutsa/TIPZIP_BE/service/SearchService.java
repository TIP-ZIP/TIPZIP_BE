package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSearchResponseDTO;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.repository.MemberRepository;
import mutsa.TIPZIP_BE.repository.PostRepository;
import mutsa.TIPZIP_BE.repository.ScrapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final PostRepository postRepository;
    private final ScrapRepository scrapRepository;
    private final MemberService memberService;

    @Transactional
    public List<PostSearchResponseDTO> searchPosts(String searchKeyword,List<String> tags,String sort){


        System.out.println("searchKeyword: " + searchKeyword);
        System.out.println("tags: " + (tags == null ? "null" : tags));
        if((searchKeyword==null || searchKeyword.isEmpty()) && (tags == null || tags.isEmpty())){
            throw new IllegalArgumentException("검색어 또는 태그를 입력해주세요");
        }
        List<Post> posts;

        if ((searchKeyword != null && !searchKeyword.isEmpty()) && tags != null && !tags.isEmpty()) {
            // 검색어와 태그 모두가 있는 경우(검색어,태그 모두 충족하는 글만 반환)
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
        //정렬 기준에 따라 리스트 정렬
        Comparator<Post> comparator=getComparator(sort);
        posts.sort(comparator);
        return posts.stream()
                .map(PostSearchResponseDTO::fromPost) // 새로운 DTO 매핑
                .collect(Collectors.toList());
                //.map(PostResponseDTO::new)
                //.map(post -> new PostResponseDTO(post, scrapRepository.existsByPostAndMemberEntity(post, member)))
                //.collect(Collectors.toList());
    }
    private Comparator<Post> getComparator(String sort){
        if ("recent".equalsIgnoreCase(sort) || sort == null || sort.isEmpty()) {
            // 최신순 (createdAt 기준 내림차순)
            return Comparator.comparing(Post::getCreatedAt).reversed();
        } else if ("oldest".equalsIgnoreCase(sort)) {
            // 오래된 순 (createdAt 기준 오름차순)
            return Comparator.comparing(Post::getCreatedAt);
        } else if ("popular".equalsIgnoreCase(sort)) {
            // 스크랩 많은 순 (scrapCount 기준 내림차순)
            return Comparator.comparing(Post::getScrapCount).reversed();
        }
        // 기본값 (최신순)
        return Comparator.comparing(Post::getCreatedAt).reversed();
    }
}
