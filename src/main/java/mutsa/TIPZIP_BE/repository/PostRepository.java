package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}