package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, ScrapId> {
    Long countByFolder(Folder folder);
    Long countByCategoryId(long categoryId);

    Optional<Scrap> findByPostAndMemberEntity(Post post, MemberEntity member);
//    void deleteById(ScrapId scrapId);

    @Query("SELECT s.post FROM Scrap s WHERE s.categoryId = :categoryId AND s.memberEntity = :member")
    List<Post> findByCategoryIdAndMember(@Param("categoryId") long categoryId, @Param("member") MemberEntity member);

    @Query("SELECT s.post FROM Scrap s WHERE s.folder = :folder AND s.memberEntity = :member")
    List<Post> findByFolderAndMember(@Param("folder") Folder folder, @Param("member") MemberEntity member);
}
