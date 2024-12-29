package mutsa.TIPZIP_BE.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderCountResponseDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderRequestsDTO;
import mutsa.TIPZIP_BE.dto.FolderDTO.FolderResponseDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.entity.Category;
import mutsa.TIPZIP_BE.entity.Folder;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Post;
import mutsa.TIPZIP_BE.repository.CategoryRepository;
import mutsa.TIPZIP_BE.repository.FolderRepository;
import mutsa.TIPZIP_BE.repository.PostRepository;
import mutsa.TIPZIP_BE.repository.ScrapRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // 로거
@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final MemberService memberService;
    private final ScrapRepository scrapRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public FolderResponseDTO createFolder(String token, FolderRequestsDTO folderRequestsDTO) {

        // 현재 로그인 중인 사용자 정보
        MemberEntity member = memberService.getUserFromToken(token);

        Folder folder = Folder.builder()
                .folderName(folderRequestsDTO.folder_name())
                .memberEntity(member)
                .build();

        folderRepository.save(folder);
        log.info("Folder Id (name) : {} ({}) is saved.", folder.getId(), folder.getFolderName());

        return new FolderResponseDTO(folder);
    }

    public List<FolderCountResponseDTO> getFolderList(boolean is_my, String token) {

        MemberEntity member = memberService.getUserFromToken(token);

        List<FolderCountResponseDTO> folderCountResponseDTOList = new ArrayList<>();

        if (is_my) {
            // 나만의 폴더 스크랩 수 조회
            List<Folder> folderList = folderRepository.findByMemberEntity(member);
            for (Folder folder : folderList) {
                long count = scrapRepository.countByFolder(folder);
                folderCountResponseDTOList.add(new FolderCountResponseDTO(folder.getFolderName(), count, folder.getId()));
            }
        } else {
            // 카테고리별 폴더 스크랩 수 조회
            List<String> categoryList = Arrays.asList("정리/공간 활용", "주방", "청소", "건강", "IT", "뷰티&패션", "여가&휴식", "로컬", "기타");
            for (String categoryName : categoryList) {
                Category category = categoryRepository.findByCategoryName(categoryName)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 category 입니다."));
                long count = scrapRepository.countByCategoryIdAndMemberEntity(category.getId(), member);
                folderCountResponseDTOList.add(new FolderCountResponseDTO(categoryName, count, null));
            }
        }
        return folderCountResponseDTOList;
    }

    @Transactional
    public FolderResponseDTO updateFolder(long id, String token, FolderRequestsDTO folderRequestsDTO) {
        MemberEntity member = memberService.getUserFromToken(token);

        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 folder 입니다."));
        
        if ( member.equals(folder.getMemberEntity())){ // 사용자 검증
            folder.setFolderName(folderRequestsDTO.folder_name());
            log.info("Folder Id : {} is changed.", folder.getId());
        } else {
            throw new RuntimeException("해당 folder 사용자가 아닙니다.");
        }

        // 더티 체킹
        return new FolderResponseDTO(folder);
    }

    @Transactional
    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 folder 입니다."));

        folderRepository.delete(folder);
    }
}
