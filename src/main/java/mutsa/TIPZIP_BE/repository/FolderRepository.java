package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    Optional<Folder> findByFolderName(String name);
    Optional<Folder> findByFolderNameAndMemberEntity(String name, MemberEntity member);
    List<Folder> findByMemberEntity(MemberEntity memberEntity);
}
