package mutsa.TIPZIP_BE.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderRequestsDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderResponseDTO;
import mutsa.TIPZIP_BE.dto.MemberDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.MyPostDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostSimpleDTO;
import mutsa.TIPZIP_BE.entity.*;
import mutsa.TIPZIP_BE.repository.*;
import org.hibernate.query.Order;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j // 로거
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final FollowRepository followRepository;

    @Transactional
    public PostResponseDTO createPost(String token, PostRequestsDTO postRequestsDTO) {

        Category category = categoryRepository.findByCategoryName(postRequestsDTO.category())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Category 입니다 : " + postRequestsDTO.category()));

        // 현재 로그인 중인 사용자 정보 가져오기
        MemberEntity member = memberService.getUserFromToken(token);

        Post post = Post.builder()
                .title(postRequestsDTO.title())
                .category(category)
                .memberEntity(member)
                .content(postRequestsDTO.content())
                .link_url(postRequestsDTO.link_url())
                .thumbnail_url(postRequestsDTO.thumbnail_url())
                .scrapCount(0)
                .build();

        postRepository.save(post);
        log.info("Post Id : {} is saved.", post.getId());

        // 태그 처리
        setTag(postRequestsDTO, post);

        return new PostResponseDTO(post);
    }

    private void setTag(PostRequestsDTO postRequestsDTO, Post post) {
        postRequestsDTO.tag().forEach(tagName -> {
            // 데이터베이스에서 태그 조회
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다 : " + tagName));

            post_tag postTag = post_tag.builder()
                    .post(post)
                    .tag(tag)
                    .build();

            // 중간 테이블 저장 (PostTag)
            postTagRepository.save(postTag);

            // post의 배열에 tag 추가
            post.addPostTag(postTag);
        });
    }


    // post entity 반환
    public Post getOnePost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 post 입니다."));
    }

    // Post List -> SimpleDTO List 변환 method
    private static List<PostSimpleDTO> postListToSimpleDTO(List<Post> postList) {
        return postList.stream()
                .map(PostSimpleDTO::new)
                .collect(Collectors.toList());
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
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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

    // 팔로잉 유저 글 조회
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PostSimpleDTO> getFollowingPostsList(String token, String sort, Long categoryId){
        MemberEntity follower = memberService.getUserFromToken(token);
/*
        List<MemberEntity> followingMembers = followRepository.findByFollower(follower.getUserId());
        if(followingMembers.isEmpty()){
            throw new RuntimeException("팔로잉 하는 유저가 존재하지 않습니다.");
        }

 */
        // `follower.getUserId()`로 호출
        List<Follow> followingRelationships = followRepository.findByFollower_UserId(follower.getUserId());
        if (followingRelationships.isEmpty()) {
            throw new RuntimeException("팔로잉 하는 유저가 존재하지 않습니다.");
        }


        // 팔로잉 유저 추출
        List<MemberEntity> followingMembers = followingRelationships.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        List<Post> followingPostsList = followingMembers.stream()
                .flatMap(member -> {
                    if (categoryId != null) {
                        Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new RuntimeException("존재하지 않는 category ID 입니다."));
                        return postRepository.findByCategoryAndMemberEntity(category, member).stream();
                    } else {
                        return postRepository.findByMemberEntity(member).stream();
                    }
                })
                .collect(Collectors.toList());

        if(followingPostsList.isEmpty()){ throw new RuntimeException("팔로잉 user post가 존재하지 않습니다."); }

        followingPostsList.sort(getComparator(sort));

        return postListToSimpleDTO(followingPostsList);
    }

    // 인증 유저 글 조회
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PostSimpleDTO> getCertPostsList(String sort, Long categoryId){
        List<MemberEntity> certMembers = memberRepository.findByBadgeTrue();
        if(certMembers.isEmpty()){
            throw new RuntimeException("인증 user가 존재하지 않습니다.");
        }

        List<Post> certPostsList = certMembers.stream()
                .flatMap(member -> {
                    if (categoryId != null) {
                        Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new RuntimeException("존재하지 않는 category ID 입니다."));
                        return postRepository.findByCategoryAndMemberEntity(category, member).stream();
                    } else {
                        return postRepository.findByMemberEntity(member).stream();
                    }
                })
                .collect(Collectors.toList());

        if(certPostsList.isEmpty()){ throw new RuntimeException("인증 user post가 존재하지 않습니다."); }

        certPostsList.sort(getComparator(sort));

        return postListToSimpleDTO(certPostsList);
    }


    // 마이페이지 글 조회
    public List<MyPostDTO> getMyposts(Long id){
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 user 입니다."));

        List<Post> myPostsList = postRepository.findByMemberEntity(memberEntity);

        return myPostsList.stream()
                .map(MyPostDTO::new)
                .collect(Collectors.toList());
    }


    // Post 수정
    @Transactional
    public PostResponseDTO updatePost(Long id, String token, PostRequestsDTO postRequestsDTO) {
        Post post = getOnePost(id);

        MemberEntity member = memberService.getUserFromToken(token);
        if ( !member.equals(post.getMemberEntity())){ // 사용자 검증
            throw new RuntimeException("해당 folder 사용자가 아닙니다.");
        }
        else { // update 진행
            if (postRequestsDTO.title() != null) { post.setTitle(postRequestsDTO.title()); }
            if (postRequestsDTO.category() != null) {
                Category category = categoryRepository.findByCategoryName(postRequestsDTO.category())
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 Category 입니다 : " + postRequestsDTO.category()));
                post.setCategory(category); }
            if (postRequestsDTO.tag() != null) {
                setTag(postRequestsDTO, post);
            }
            if (postRequestsDTO.content() != null) { post.setContent(postRequestsDTO.content()); }
            if (postRequestsDTO.link_url() != null) { post.setLink_url(postRequestsDTO.link_url()); }
            if (postRequestsDTO.thumbnail_url() != null) { post.setThumbnail_url(postRequestsDTO.thumbnail_url()); }
        }

        log.info("Post Id : {} is changed.", id);
        // 더티 체킹
        return new PostResponseDTO(post);
    }


    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 post 입니다."));

        postRepository.delete(post);
    }

}
