package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tag_id")
    private long id;

    private String tag_name;

    // 연관관계 매핑
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
    private List<post_tag> postTags = new ArrayList<>();
}
