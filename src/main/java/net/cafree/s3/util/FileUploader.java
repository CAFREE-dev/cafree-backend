package net.cafree.s3.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploader {
    List<String> upload(List<MultipartFile> multipartFiles, Long feedId) throws IOException;
    void deleteFromS3(List<String> urls);
}
