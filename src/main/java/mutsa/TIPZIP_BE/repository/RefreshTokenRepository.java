package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByMember(MemberEntity member);
    Optional<RefreshToken> findByMember_Email(String email);
    //토큰 값으로 특정 refresh token검색
    Optional<RefreshToken> findByToken(String token);
}


