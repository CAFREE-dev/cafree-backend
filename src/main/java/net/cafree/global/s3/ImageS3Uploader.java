package net.cafree.global.s3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.cafree.global.s3.dto.ImageDeleteRequest;
import net.cafree.global.s3.dto.ImageUploadRequest;
import net.cafree.global.s3.dto.S3File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ImageS3Uploader {
	@Value("${cloud.aws.s3-bucket}")
	@Getter
	private String s3Bucket;

	private final AmazonS3 amazonS3Client;

	public List<S3File> upload(ImageUploadRequest imageUploadRequest) throws IOException {
		List<S3File> s3Files = new ArrayList<>();

		for (int i = 0; i < imageUploadRequest.images().size(); i++) {
			MultipartFile image = imageUploadRequest.images().get(i);

			String fileName = createFileName(
				imageUploadRequest.domain(),
				imageUploadRequest.memberId(),
				imageUploadRequest.imageNames().get(i));
			System.out.println(imageUploadRequest.imageNames().get(i));
			s3Files.add(new S3File(fileName, putS3(image, fileName, getObjectMetadata(image))));
		}

		return s3Files;
	}

	public void deleteFiles(ImageDeleteRequest imageDeleteRequest) {
		List<String> fileNames = imageDeleteRequest.getFileNamesWithPath();
		fileNames.forEach(this::deleteFromS3);
	}

	private void deleteFromS3(String fileName) {
		validateFromS3(fileName);
		amazonS3Client.deleteObject(new DeleteObjectRequest(s3Bucket, fileName));
	}

	private void validateFromS3(String fileName) {
		if (!amazonS3Client.doesObjectExist(s3Bucket, fileName)) {
			throw new AmazonS3Exception("Object " + fileName + " does not exist");
		}
	}

	private String putS3(MultipartFile multipartFile, String fileName, ObjectMetadata objectMetadata)
		throws IOException {
		amazonS3Client.putObject(
			new PutObjectRequest(s3Bucket, fileName, multipartFile.getInputStream(), objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return getS3Url(fileName);
	}

	private String getS3Url(String fileName) {
		return amazonS3Client.getUrl(s3Bucket, fileName).toString();
	}

	private String createFileName(String domain, Long memberId, String fileName) {
		return "users/" + memberId + "/" + domain + "/" + fileName;
	}

	private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(multipartFile.getContentType());
		objectMetaData.setContentLength(multipartFile.getSize());

		return objectMetaData;
	}
}