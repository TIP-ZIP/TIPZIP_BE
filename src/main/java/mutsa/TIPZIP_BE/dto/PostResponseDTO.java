package mutsa.TIPZIP_BE.dto;

// 미리보기 형식으로 내보내는 경우 (이미지 정보, link 정보 불필요)
public record PostResponseDTO(Long id, String title, String content, String author, String date) {
}
