package net.cafree.global.s3.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ImageUploadRequest(@Nonnull List<MultipartFile> images,
								 @Nonnull List<String> imageNames,
								 @NotBlank String domain,
								 @NotBlank Long memberId) {
	@Builder
	public ImageUploadRequest {
	}
}
