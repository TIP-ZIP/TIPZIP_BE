package mutsa.TIPZIP_BE.dto.PostDTO;

import mutsa.TIPZIP_BE.entity.Post;

import java.util.ArrayList;
import java.util.List;

// 상세보기
public record PostResponseDTO(Long id, String author, Long user_id, String profile_image, boolean badge, List<String> tag, String createdAt, String title, String category, String content, boolean scrap, long scrapCount, String link_url, String thumbnail_url) {

    public PostResponseDTO(Post post) {
        this(post.getId(), post.getMemberEntity().getUsername(), post.getMemberEntity().getUserId(), post.getMemberEntity().getProfile_image(),
                post.getMemberEntity().getBadge(), post.getPostTags(),post.getCreatedAt().toString(), post.getTitle(), post.getCategory().getCategoryName(),
                post.getContent(),post.isScrapped(), post.getScrapCount(), post.getLink_url(), post.getThumbnail_url());
    }
}