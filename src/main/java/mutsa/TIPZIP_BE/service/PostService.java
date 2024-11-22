package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.PostRequestsDTO;
import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.entity.Tag;
import mutsa.TIPZIP_BE.repository.CategoryRepository;
import mutsa.TIPZIP_BE.repository.PostRepository;
import mutsa.TIPZIP_BE.repository.PostTagRepository;
import mutsa.TIPZIP_BE.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j // 로거
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    public Post createPost(PostRequestsDTO postRequestsDTO) {

        // 썸네일 없으면 첫 번째 이미지로 설정
        String thumbnailUrl = postRequestsDTO.thumbnail_url();
        if (thumbnailUrl == null && !postRequestsDTO.images().isEmpty()) {
            thumbnailUrl = postRequestsDTO.images().get(0);
        }

        Category category = categoryRepository.findByName(postRequestsDTO.category())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다: " + postRequestsDTO.category()));;

        Post post = Post.builder()
                .title(postRequestsDTO.title())
                .category(category)
                .content(postRequestsDTO.content())
                .link_url(postRequestsDTO.link_url())
                .thumbnail_url(thumbnailUrl)
                .build();

        postRepository.save(post);

        // 태그 처리
        postRequestsDTO.tag().forEach(tagName -> {
            // 데이터베이스에서 태그 조회
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다: " + tagName));

            // 중간 테이블 저장 (PostTag)
            postTagRepository.save(PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build());
        });

        return post;
    }

    public List<Post> getPostList(){
        return postRepository.findAll();
    }

    public Optional<Post> getOnePost(Long postId) {
        return postRepository.findById(postId);
    }
}
