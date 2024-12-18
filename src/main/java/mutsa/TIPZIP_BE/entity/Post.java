package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class) // created_at에 사용
@Entity
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    // user 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MemberEntity memberEntity; // 해당 post를 작성한 유저의 id
    // category 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // tag 매핑
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<post_tag> postTags = new ArrayList<>();
    // scrap 매핑
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Scrap> scraps = new ArrayList<>();

    private String title;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    private String content;
    private String link_url;
    private String thumbnail_url;
    private boolean scrap; // 스크랩 여부
    private long scrapCount; // 스크랩 수

    public ArrayList<String> getPostTags(long postId) {
        ArrayList<String> tags = new ArrayList<>();

        for (post_tag postTag : postTags) {
            tags.add(postTag.getTag().getTagName()); // Tag 객체에서 태그 이름 추출
        }
        return tags;
    }

}