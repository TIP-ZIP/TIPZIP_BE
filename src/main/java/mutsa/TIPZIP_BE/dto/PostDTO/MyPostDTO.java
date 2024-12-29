package mutsa.TIPZIP_BE.dto.PostDTO;

import mutsa.TIPZIP_BE.entity.Post;

// 마이페이지용
public record MyPostDTO(Long id, String title, boolean scrap, long scrapCount, String thumbnail_url, String category) {

    public MyPostDTO(Post post, boolean isScrapped) {
        this(post.getId(), post.getTitle(), isScrapped, post.getScrapCount(), post.getThumbnail_url(), post.getCategory().getCategoryName());
    }
}