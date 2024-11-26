package mutsa.TIPZIP_BE.S3Storage;

import com.amazonaws.services.ec2.model.S3Storage;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/uploadImageFile")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        JsonObject jsonObject = new JsonObject();

        try {
            String imageUrl = s3Service.uploadToS3(multipartFile);
            jsonObject.addProperty("S3url", imageUrl);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(jsonObject);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 에러");
        }
    }

}
