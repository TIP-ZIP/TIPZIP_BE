package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, long> {
    Optional<Category> findByName(String name);
}
