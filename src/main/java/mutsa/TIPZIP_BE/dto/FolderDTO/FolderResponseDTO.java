package mutsa.TIPZIP_BE.dto.FolderDTO;

import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.entity.Post;

public record FolderResponseDTO(Long id, String folderName) {

    public FolderResponseDTO(Folder folder) {
        this(folder.getId(), folder.getFolder_name());
    }
}
