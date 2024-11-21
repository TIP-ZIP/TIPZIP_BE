package mutsa.TIPZIP_BE.dto;

import java.util.List;

public record PostRequestsDTO(String title, String category, List tag, String content, List images, String link_url, String thumbnail_url) {

}
