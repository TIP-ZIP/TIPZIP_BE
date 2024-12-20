package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Folder, Long> {
    Long countByFolder(Folder folder);
    Long countByCategory(Category category);
}
