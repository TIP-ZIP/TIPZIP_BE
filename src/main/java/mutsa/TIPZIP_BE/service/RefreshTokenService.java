package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.RefreshToken;
import mutsa.TIPZIP_BE.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(MemberEntity member,String token,long expiryDurationInSeconds) {
        //중복 방지 위해 기존 refreshtoken삭제
        refreshTokenRepository.deleteByMember(member);
        //새 refresh token생성
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setMember(member);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(expiryDurationInSeconds));
        refreshToken.setCreatedAt(LocalDateTime.now());

        refreshTokenRepository.save(refreshToken);
    }
    //리프레시토큰 조회 메서드
    public RefreshToken getRefreshToken(String email) {
        return refreshTokenRepository.findByMember_Email(email)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 존재하지 않습니다."));
    }
    @Transactional
    public void deleteRefreshToken(String email) {
        refreshTokenRepository.deleteByMember_Email(email);
    }
}
