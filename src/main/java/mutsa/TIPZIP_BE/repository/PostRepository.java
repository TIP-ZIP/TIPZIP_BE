package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAll(Sort sort);
    List<Post> findByCategory(Category category, Sort sort);
    List<Post> findByCategoryIn(List<Category> categories, Sort sort);

    // 유저별 post들 반환
    List<Post> findByMemberEntity(MemberEntity user);
    List<Post> findByCategoryInAndMemberEntity(List<Category> categories, MemberEntity user);
    
    // 팔로잉 유저들 post들 반환에 사용
    List<Post> findByCategoryInAndMemberEntityIn(List<Category> categories, List<MemberEntity> members);
    List<Post> findByMemberEntityIn(List<MemberEntity> members);

    // 제목 또는 내용에 검색어가 포함된 post데이터 반환(태그 없고 검색어만 존재시 사용)
    List<Post> findByTitleContainingOrContentContaining(String title, String content);

    // 태그로 검색 (Post와 Tag를 JOIN)
    @Query("SELECT p FROM Post p JOIN p.postTags pt JOIN pt.tag t WHERE t.tagName IN :tags")
    List<Post> findByTagsIn(List<String> tags);

    // 검색어와 태그를 동시에 검색
    @Query("SELECT p FROM Post p JOIN p.postTags pt JOIN pt.tag t WHERE " +
            "(p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND t.tagName IN :tags")
    List<Post> findByTitleContainingOrContentContainingAndTagsIn(@Param("keyword") String keyword,
                                                                 @Param("keyword")String Keyword,
                                                                 @Param("tags") List<String> tags);
}