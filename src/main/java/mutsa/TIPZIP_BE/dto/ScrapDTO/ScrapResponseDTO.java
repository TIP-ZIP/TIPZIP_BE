package mutsa.TIPZIP_BE.dto.ScrapDTO;

import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.entity.Scrap;

public record ScrapResponseDTO(long post_id, String folder, long category) {

    public ScrapResponseDTO(Scrap scrap) {
        this(scrap.getPost().getId(), (scrap.getFolder() != null ? scrap.getFolder().getFolderName() : null), scrap.getCategoryId());
    }
}
