package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity // JPA Entity로 선언
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RefreshToken {
    @Id // 기본키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정 (필요에 따라 변경 가능)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable =false)
    private MemberEntity member;

    @Column(nullable = false, unique = true)
    private String token; // Refresh Token 값

    @Column(nullable = false)
    private LocalDateTime expiryDate; // 만료 시간

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 발급 시간
}
