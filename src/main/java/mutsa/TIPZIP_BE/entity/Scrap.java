package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Scrap")
@IdClass(ScrapId.class)
public class Scrap {

    private long categoryId;

    // 연관관계 매핑이자 Id
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MemberEntity memberEntity;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // folder 연관관계 매핑
    // optional 항목임
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "folder_id", nullable = true)
    private Folder folder;
}
