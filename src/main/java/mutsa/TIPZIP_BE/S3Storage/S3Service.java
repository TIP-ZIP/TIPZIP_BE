package mutsa.TIPZIP_BE.S3Storage;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 등록
    public String uploadToS3(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String savedFileName = UUID.randomUUID() + originalFileName;

        try {
            return uploadFileToS3(multipartFile, savedFileName);
        } catch (AmazonServiceException e) {
            log.error("Amazon service error: " + e.getErrorMessage());
            throw e;
        } catch (AmazonClientException e) {
            log.error("Amazon client error: " + e.getMessage());
            throw e;
        }
    }
    @Transactional
    public String uploadFileToS3(MultipartFile multipartFile, String savedFileName) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
//        log.info("set metadata: " + metadata);

        // S3에 객체 등록 : putObject(버킷명, 파일명, 파일데이터, 메타데이터)
        amazonS3.putObject(new PutObjectRequest(bucket, savedFileName, multipartFile.getInputStream(), metadata).withCannedAcl(CannedAccessControlList.PublicRead));
//        log.info("Saved file to s3: " + savedFileName);

        // URL 설정
        String imageUrl = amazonS3.getUrl(bucket, savedFileName).toString();
        // 한글이나 특수문자 깨짐을 방지하기 위해 decode
        return URLDecoder.decode(imageUrl, "utf-8");
    }


    // 삭제
    private String getKeyFromImageAddress(String imageUrl){
        try{
            URL url = new URL(imageUrl);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            String key = decodingKey.substring(9); // 앞의 '/tip.zip/' 제거
            log.info(key);
            return key;
        }catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteImageFromS3(String imageUrl) {
        String key = getKeyFromImageAddress(imageUrl);

        try{
            amazonS3.deleteObject(bucket, key);
        }catch (AmazonServiceException e){
            log.error("Amazon service error: " + e.getErrorMessage());
        }
    }
}
