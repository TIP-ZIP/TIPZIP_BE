package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.repository.FolderRepository;
import org.springframework.stereotype.Service;

@Slf4j // 로거
@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;


}
