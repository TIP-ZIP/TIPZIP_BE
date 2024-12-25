package mutsa.TIPZIP_BE.dto.PostDTO;

import mutsa.TIPZIP_BE.entity.Post;

// 미리보기 형식으로 내보내는 경우
public record PostSimpleDTO(Long id, String author, boolean badge, String title, boolean scrap, long scrapCount, String thumbnail_url) {

    public PostSimpleDTO(Post post) {
        this(post.getId(), post.getMemberEntity().getUsername(), post.getMemberEntity().getBadge(),
                post.getTitle(), post.isScrapped(), post.getScrapCount(), post.getThumbnail_url());
    }
}
