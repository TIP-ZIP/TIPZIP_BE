package mutsa.TIPZIP_BE.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSimpleDTO;
import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final MemberRepository memberRepository;

    @Transactional
    public Post createPost(PostRequestsDTO postRequestsDTO) {

        Category category = categoryRepository.findByCategoryName(postRequestsDTO.category())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리 입니다 : " + postRequestsDTO.category()));;

        // 현재 로그인 중인 사용자 정보 가져오기 (메소드 필요)
//        MemberService user =

        Post post = Post.builder()
                .title(postRequestsDTO.title())
                .category(category)
//                .user(user)
                .content(postRequestsDTO.content())
                .link_url(postRequestsDTO.link_url())
                .thumbnail_url(postRequestsDTO.thumbnail_url())
                .scrap(false) // 스크랩 여부 default:false
                .scrapCount(0)
                .build();

        postRepository.save(post);
        log.info("Post Id : {} is saved.", post.getId());

        // 태그 처리
//        postRequestsDTO.tag().forEach(tagName -> {
//            // 데이터베이스에서 태그 조회
//            Tag tag = tagRepository.findByTagName(tagName)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다 : " + tagName));

            // 중간 테이블 저장 (PostTag)
//            postTagRepository.save(post_tag.builder()
//                    .post(post)
//                    .tag(tag)
//                    .build());
//        });

        return post;
    }

    // post 하나 반환
    public Optional<Post> getOnePost(Long postId) {
        return postRepository.findById(postId);
    }

    // Post List -> SimpleDTO List 변환 method
    private static List<PostSimpleDTO> postListToSimpleDTO(List<Post> postList) {
        List<PostSimpleDTO> postResponseList = new ArrayList<>();
        for (Post post : postList) {
            PostSimpleDTO dto = new PostSimpleDTO(post);
            postResponseList.add(dto);
        }
        return postResponseList;
    }

    // 전체 글 조회
    public List<PostSimpleDTO> getPostList(){

        List<Post> postList = postRepository.findAll();
        return postListToSimpleDTO(postList);
    }

    // 인증 유저 글 조회
    public List<PostSimpleDTO> getCertPostsList(){
        List<MemberEntity> certMembers = memberRepository.findByBadgeTrue();

        List<Post> certPostsList = new ArrayList<>();
        for (MemberEntity member : certMembers) {
            List<Post> posts = postRepository.findByUserId(member.getUser_id());
            certPostsList.addAll(posts);
        }
        return postListToSimpleDTO(certPostsList);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

}
