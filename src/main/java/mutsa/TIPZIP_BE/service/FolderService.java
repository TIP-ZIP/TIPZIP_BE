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

    public List<FolderCountResponseDTO> getFolderList(boolean is_my) {

        List<FolderCountResponseDTO> folderCountResponseDTOList = new ArrayList<>();

        if (is_my) {
            // 나만의 폴더 스크랩 수 조회
            List<Folder> folderList = folderRepository.findAll();
            for (Folder folder : folderList) {
                long count = scrapRepository.countByFolder(folder);
                folderCountResponseDTOList.add(new FolderCountResponseDTO(folder, count));
            }
        } else {
            // 카테고리별 폴더 스크랩 수 조회
            List<String> categoryList = Arrays.asList("정리/공간 활용", "주방", "청소", "건강", "IT", "뷰티&패션", "여가&휴식", "로컬", "기타");
            for (String categoryName : categoryList) {
                Category category = categoryRepository.findByCategoryName(categoryName)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 category 입니다."));
                long count = scrapRepository.countByCategory(category);
                folderCountResponseDTOList.add(new FolderCountResponseDTO(categoryName, count));
            }
        }
        return folderCountResponseDTOList;
    }
}
