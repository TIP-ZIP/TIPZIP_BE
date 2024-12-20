package mutsa.TIPZIP_BE.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderRequestsDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.repository.FolderRepository;
import org.springframework.stereotype.Service;

@Slf4j // 로거
@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final MemberService memberService;

    @Transactional
    public FolderResponseDTO createFolder(String token, FolderRequestsDTO folderRequestsDTO) {

        // 현재 로그인 중인 사용자 정보 가져오기
        MemberEntity member = memberService.getUserFromToken(token);

        Folder folder = Folder.builder()
                .folder_name(folderRequestsDTO.folderName())
                .memberEntity(member)
                .build();

        folderRepository.save(folder);
        log.info("Folder Id : {} is saved.", folder.getId());

        return new FolderResponseDTO(folder);
    }


}
