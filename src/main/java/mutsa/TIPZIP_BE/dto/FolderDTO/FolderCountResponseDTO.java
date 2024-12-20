package mutsa.TIPZIP_BE.dto.FolderDTO;

import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.repository.ScrapRepository;

public record FolderCountResponseDTO(String folderName, Long count) {

    public FolderCountResponseDTO(Folder folder, Long count) {
        this(folder.getFolder_name(), count);
    }
}
