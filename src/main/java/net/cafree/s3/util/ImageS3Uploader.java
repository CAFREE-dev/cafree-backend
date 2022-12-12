package net.cafree.s3.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class ImageS3Uploader implements FileUploader{
    @Value("${cloud.aws.s3-bucket}")
    @Getter
    private String s3Bucket;

    @Value("${signature.file-name}")
    @Getter
    private String signature;
    private final AmazonS3 amazonS3Client;

    @Override
    public List<String> upload(List<MultipartFile> multipartFiles, Long feedId) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            imageUrls.add(putS3(
                    multipartFile,
                    createFileName(feedId, multipartFile.getOriginalFilename()),
                    getObjectMetadata(multipartFile))
            );
        }
        return imageUrls;
    }

    @Override
    public void deleteFromS3(List<String> urls) {
        urls.forEach(this::delete);
    }

    private void delete(String filename) {
        if(!amazonS3Client.doesObjectExist(s3Bucket, filename)){
            throw new AmazonS3Exception("Object " + filename + " does not exist");
        }
        amazonS3Client.deleteObject(new DeleteObjectRequest(s3Bucket, filename));
    }

    private String putS3(MultipartFile multipartFile, String fileName, ObjectMetadata objectMetadata) throws IOException {
        amazonS3Client.putObject(
                new PutObjectRequest(s3Bucket, fileName, multipartFile.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return getS3Url(fileName);
    }

    private String getS3Url(String fileName) {
        return amazonS3Client.getUrl(s3Bucket, fileName).toString();
    }

    private String createFileName(Long feedId, String originalName) {
        return feedId + "/" + NanoIdUtils.randomNanoId(new Random(), signature.toCharArray(), 10) + originalName;
    }

    private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(multipartFile.getSize());

        return objectMetaData;
    }
}
