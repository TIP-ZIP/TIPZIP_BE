package mutsa.TIPZIP_BE.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.MyPostDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSimpleDTO;
import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.repository.*;
import org.hibernate.query.Order;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    public PostResponseDTO createPost(String token, PostRequestsDTO postRequestsDTO) {

        Category category = categoryRepository.findByCategoryName(postRequestsDTO.category())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Category 입니다 : " + postRequestsDTO.category()));;

        // 현재 로그인 중인 사용자 정보 가져오기 (메소드 필요)
//        MemberDTO memberDTO = MemberService.getUserFromToken(token);

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

        PostResponseDTO postResponseDTO = new PostResponseDTO(post);
        return postResponseDTO;
    }


    // post entity 반환
    public Post getOnePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 post 입니다."));
        return post;
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

    // 정렬 옵션
    private Sort getSort(String sort) {
        switch (sort) {
            case "recent":
                return Sort.by(Sort.Order.desc("createdAt"));
            case "oldest":
                return Sort.by(Sort.Order.asc("createdAt"));
            case "popular":
                return Sort.by(Sort.Order.desc("scrapCount"));
            default:
                throw new RuntimeException("존재하지 않는 정렬 옵션입니다.");
        }
    }
    private Comparator<Post> getComparator(String sort) {
        switch (sort) {
            case "recent":
                return Comparator.comparing(Post::getCreatedAt).reversed();
            case "oldest":
                return Comparator.comparing(Post::getCreatedAt);
            case "popular":
                return Comparator.comparing(Post::getScrapCount).reversed();
            default:
                throw new RuntimeException("존재하지 않는 정렬 옵션입니다.");
        }
    }

    // 전체 글 조회
    public List<PostSimpleDTO> getPostList(String sort, Long categoryId){
        List<Post> postList;

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 category ID 입니다."));

            postList = postRepository.findByCategory(category, getSort(sort));
        }
        else { postList = postRepository.findAll(getSort(sort)); }

        return postListToSimpleDTO(postList);
    }

    // 인증 유저 글 조회
    public List<PostSimpleDTO> getCertPostsList(String sort, Long categoryId){
        List<MemberEntity> certMembers = memberRepository.findByBadgeTrue();
        if(certMembers.isEmpty()){
            throw new RuntimeException("인증 user가 존재하지 않습니다.");
        }

        List<Post> certPostsList = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 category ID 입니다."));
            for (MemberEntity member : certMembers) {
                List<Post> posts = postRepository.findByCategoryAndMemberEntity(category, member);
                certPostsList.addAll(posts);
            }
        } else {
            for (MemberEntity member : certMembers) {
                List<Post> posts = postRepository.findByMemberEntity(member);
                certPostsList.addAll(posts);
            }
        }
        if(certPostsList.isEmpty()){ throw new RuntimeException("인증 user post가 존재하지 않습니다."); }

        certPostsList.sort(getComparator(sort));

        return postListToSimpleDTO(certPostsList);
    }

    // 마이페이지 글 조회
    public List<MyPostDTO> getMyposts(Long id){
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 user 입니다."));

        List<Post> myPostsList = postRepository.findByMemberEntity(memberEntity);

        List<MyPostDTO> myPostDTOsList = new ArrayList<>();
        for (Post post : myPostsList) {
            myPostDTOsList.add(new MyPostDTO(post));
        }
        return myPostDTOsList;
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 post 입니다."));

        postRepository.delete(post);
    }

}
