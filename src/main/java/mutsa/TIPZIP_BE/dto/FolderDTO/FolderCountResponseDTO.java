package mutsa.TIPZIP_BE.dto.FolderDTO;

import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.repository.ScrapRepository;

public record FolderCountResponseDTO(String folderName, Long count, Long folder_id) {

//    public FolderCountResponseDTO(Folder folder, Long count, long folder_id) {
//        this(folder.getFolderName(), count, folder_id);
//    }
}
