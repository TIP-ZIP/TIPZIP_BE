package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByEmail(String email);
    //유저네임 중복성 검사시 사용
    boolean existsByUsername(String username);
}
