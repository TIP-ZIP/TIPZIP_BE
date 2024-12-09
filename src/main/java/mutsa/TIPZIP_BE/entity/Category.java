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
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private long id;

    private String categoryName;

    // 연관관계 매핑
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Tag> tags;
}
