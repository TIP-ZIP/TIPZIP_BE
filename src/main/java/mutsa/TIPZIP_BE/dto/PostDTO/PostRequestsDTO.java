package mutsa.TIPZIP_BE.dto.PostDTO;

import java.util.ArrayList;

public record PostRequestsDTO(String title, String category, String content, ArrayList<String> tag, String link_url, String thumbnail_url) {

}
