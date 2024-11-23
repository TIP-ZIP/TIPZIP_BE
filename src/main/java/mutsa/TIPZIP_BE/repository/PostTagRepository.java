package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.post_tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<post_tag, Long> {

}
