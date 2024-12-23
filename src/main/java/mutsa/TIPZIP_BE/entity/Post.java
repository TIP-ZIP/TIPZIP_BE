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
import java.util.stream.Collectors;

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
    private MemberEntity memberEntity; // 해당 post를 작성한 유저
    // category 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // tag 매핑
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<post_tag> postTags = new ArrayList<>();
    // scrap 매핑
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Scrap scrap;

    private String title;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    private String content;
    private String link_url;
    private String thumbnail_url;
    private long scrapCount; // 스크랩 수


    // scrap 여부
    public boolean isScrapped() {
        return this.scrap != null;
    }

    // tag 추가
    public void addPostTag(post_tag postTag) {
        this.postTags.add(postTag);
    }

    // postTags 로부터 tag 이름 목록 반환 method
    public List<String> getPostTags() {
        return postTags.stream()
                .map(postTag -> postTag.getTag().getTagName())
                .collect(Collectors.toList());
    }

}