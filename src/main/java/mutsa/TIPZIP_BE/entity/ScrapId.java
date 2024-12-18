package mutsa.TIPZIP_BE.entity;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScrapId implements Serializable {
    private Long memberEntity;
    private Long post;
}