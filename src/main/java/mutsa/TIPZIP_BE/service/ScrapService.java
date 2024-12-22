package mutsa.TIPZIP_BE.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.TIPZIP_BE.dto.PostDTO.MyPostDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostRequestsDTO;
import mutsa.TIPZIP_BE.dto.PostDTO.PostResponseDTO;
import mutsa.TIPZIP_BE.dto.ScrapDTO.ScrapRequestsDTO;
import mutsa.TIPZIP_BE.dto.ScrapDTO.ScrapResponseDTO;
import mutsa.TIPZIP_BE.entity.*;
import mutsa.TIPZIP_BE.repository.CategoryRepository;
import mutsa.TIPZIP_BE.repository.FolderRepository;
import mutsa.TIPZIP_BE.repository.PostRepository;
import mutsa.TIPZIP_BE.repository.ScrapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j // 로거
@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;
    private final FolderRepository folderRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ScrapResponseDTO createScrap(String token, ScrapRequestsDTO scrapRequestsDTO) {

        Post post = postRepository.findById(scrapRequestsDTO.post_id())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Post 입니다 : " + scrapRequestsDTO.post_id()));

        Folder folder = folderRepository.findByFolder_name(scrapRequestsDTO.folder_name())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Folder 입니다 : " + scrapRequestsDTO.folder_name()));

        // 현재 로그인 중인 사용자 정보 가져오기
        MemberEntity member = memberService.getUserFromToken(token);

        Scrap scrap = Scrap.builder()
                .categoryId(post.getCategory().getId())
                .memberEntity(member)
                .post(post)
                .folder(folder)
                .build();

        scrapRepository.save(scrap);
        log.info("Scrap is added.");

        return new ScrapResponseDTO(scrap);
    }

    public List<MyPostDTO> getScrapByCategory(long id, String token) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Category 입니다 : " + id));

        MemberEntity member = memberService.getUserFromToken(token);

        List<Post> categoryPostsList = scrapRepository.findByCategoryIdAndMember(id, member);

        return categoryPostsList.stream()
                .map(MyPostDTO::new)
                .collect(Collectors.toList());
    }

    public List<MyPostDTO> getScrapByFolder(String folderName, String token) {
        Folder folder = folderRepository.findByFolder_name(folderName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Folder 입니다 : " + folderName));

        MemberEntity member = memberService.getUserFromToken(token);

        List<Post> categoryPostsList = scrapRepository.findByFolderAndMember(folder, member);

        return categoryPostsList.stream()
                .map(MyPostDTO::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteScrap(String token, ScrapRequestsDTO scrapRequestsDTO) {
        MemberEntity member = memberService.getUserFromToken(token);

        Post post = postRepository.findById(scrapRequestsDTO.post_id())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Post 입니다 : " + scrapRequestsDTO.post_id()));;

        ScrapId scrapId = new ScrapId(member.getUser_id(), post.getId());
        scrapRepository.deleteById(scrapId);
    }
}
