package mutsa.TIPZIP_BE.dto.PostDTO;

import mutsa.TIPZIP_BE.entity.Post;

// 마이페이지용
public record MyPostDTO(Long id, String title, boolean scrap, long scrapCount, String thumbnail_url) {

    public MyPostDTO(Post post) {
        this(post.getId(), post.getTitle(), post.isScrap(), post.getScrapCount(), post.getThumbnail_url());
    }
}