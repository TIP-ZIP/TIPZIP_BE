package mutsa.TIPZIP_BE.dto.PostDTO;

import mutsa.TIPZIP_BE.entity.Post;

// 미리보기 형식으로 내보내는 경우
public record PostSimpleDTO(Long id, String author, Long user_id, String profile_image, boolean badge, String title, String category, boolean scrap, long scrapCount, String thumbnail_url) {

    public PostSimpleDTO(Post post, boolean isScrapped) {
        this(post.getId(), post.getMemberEntity().getUsername(), post.getMemberEntity().getUserId(), post.getMemberEntity().getProfile_image(),
                post.getMemberEntity().getBadge(), post.getTitle(), post.getCategory().getCategoryName(), isScrapped, post.getScrapCount(), post.getThumbnail_url());
    }
}
