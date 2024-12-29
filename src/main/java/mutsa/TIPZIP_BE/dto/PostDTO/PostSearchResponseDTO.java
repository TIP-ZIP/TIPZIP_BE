package mutsa.TIPZIP_BE.dto.PostDTO;


import mutsa.TIPZIP_BE.entity.Post;

public record PostSearchResponseDTO(
        Long id,
        String title,
        String category,
        String createdAt,
        String content
) {
    public static PostSearchResponseDTO fromPost(Post post) {
        return new PostSearchResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getCategory().getCategoryName(),
                post.getCreatedAt().toString(),
                post.getContent()
        );
    }
}
