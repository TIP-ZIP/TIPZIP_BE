package mutsa.TIPZIP_BE.dto.PostDTO;

import mutsa.TIPZIP_BE.entity.Post;

import java.util.ArrayList;

// 상세보기
public record PostResponseDTO(Long id, String createdAt, String title, String content, boolean scrap, long scrapCount, String link_url, String thumbnail_url) {
//String author, boolean badge, ArrayList<String> tag,
    public PostResponseDTO(Post post) {
        this(post.getId(),
                post.getCreatedAt().toString(), post.getTitle(), post.getContent(),
                post.isScrap(), post.getScrapCount(), post.getLink_url(), post.getThumbnail_url());
    }
}
//post.getMemberEntity().getUsername(),post.getMemberEntity().getBadge(), post.getPostTags(post.getId()),