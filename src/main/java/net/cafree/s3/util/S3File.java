package net.cafree.s3.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class S3File {
    private String fileName;
    private String fileUrl;
}
