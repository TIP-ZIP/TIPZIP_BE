package mutsa.TIPZIP_BE.dto;

import java.util.ArrayList;
import java.util.List;

public record PostRequestsDTO(String title, String category, ArrayList<String> tag, String content, String link_url, String thumbnail_url) {

}
