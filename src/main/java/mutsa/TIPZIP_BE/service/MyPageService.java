package mutsa.TIPZIP_BE.service;

import lombok.RequiredArgsConstructor;
import mutsa.TIPZIP_BE.S3Storage.S3Service;
import mutsa.TIPZIP_BE.dto.MyPageResponseDTO;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.jwt.JwtTokenProvider;
import mutsa.TIPZIP_BE.repository.FollowRepository;
import mutsa.TIPZIP_BE.repository.MemberRepository;
import mutsa.TIPZIP_BE.service.AuthService.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final S3Service s3Service;
    private final FollowRepository followRepository;
    public MyPageResponseDTO getMyPage(String token){
        String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //아래는 예시이며, post, follow관련 로직 구현 후 수정필요.
        //int postCount = postRepository.countByUserId(memberEntity.getUser_id()); // 게시글 수
        int followerCount = followRepository.countByFollowing(memberEntity); // 팔로워 수
        int followingCount = followRepository.countByFollower(memberEntity); // 팔로잉 수

        //MyPageResponseDTO myPageResponseDTO = MyPageResponseDTO.fromMemberEntity(memberEntity);

        //MyPageResponseDTO myPageResponseDTO = MyPageResponseDTO.fromMemberEntity(memberEntity, postCount, followerCount, followingCount);
        MyPageResponseDTO myPageResponseDTO = MyPageResponseDTO.fromMemberEntity(memberEntity,followerCount, followingCount);
        return myPageResponseDTO;
    }
    public void updateUsername(String token, String newUsername){
        String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        //유저네임 중복성 검사
        if(memberRepository.existsByUsername(newUsername)){
            throw new IllegalArgumentException("유저네임이 중복되었습니다.");
        }
        memberEntity.setUsername(newUsername);
        memberRepository.save(memberEntity);
    }
    public void checkUsernameExists(String username){
        if (memberRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("유저네임이 중복되었습니다.");
        }
    }
    public void updateMessage(String token, String newMessage){
        String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        MemberEntity memberEntity =memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        memberEntity.setMessage(newMessage);
        memberRepository.save(memberEntity);
    }
    public String updateProfileImage(String token, MultipartFile file){
        String email=jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        //jwt토큰으로 사용자 확인
        if (email == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        //이메일로 사용자 정보조회
        MemberEntity memberEntity =memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));
        //기존 이미지 존재시 삭제
        String oldImageUrl=memberEntity.getProfile_image();
        if(oldImageUrl!=null&& !oldImageUrl.isEmpty()){
            s3Service.deleteImageFromS3(oldImageUrl);
        }
        //새 이미지 업로드
        String newImageUrl;
        try {
            newImageUrl = s3Service.uploadToS3(file);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }
        memberEntity.setProfile_image(newImageUrl);
        memberRepository.save(memberEntity);
        return newImageUrl;

    }
    public MyPageResponseDTO getOtherUserPage(Long userId){
        MemberEntity memberEntity=memberRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        //아래는 예시이며, post, follow관련 로직 구현 후 수정필요.
        //int postCount = postRepository.countByUserId(memberEntity.getUser_id()); // 게시글 수
        int followerCount = followRepository.countByFollowing(memberEntity);  // 팔로워 수
        int followingCount = followRepository.countByFollower(memberEntity); // 팔로잉 수
        return MyPageResponseDTO.fromMemberEntity(memberEntity,followerCount, followingCount);
    }
}
