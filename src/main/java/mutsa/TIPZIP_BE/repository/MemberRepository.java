package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository {
    Optional<MemberEntity> findByEmail(String email);
}
