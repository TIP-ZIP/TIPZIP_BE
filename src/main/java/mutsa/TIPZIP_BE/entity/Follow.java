package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Transactional
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 설정
    private Long id;

    //팔로우 하는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private MemberEntity follower;

    // 팔로우 당하는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private MemberEntity following;

    // 생성자
    public Follow(MemberEntity follower, MemberEntity following) {
        this.follower = follower;
        this.following = following;
    }
}
