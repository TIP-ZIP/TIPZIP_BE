package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Long countByFolder(Folder folder);
    Long countByCategoryId(long categoryId);
}
