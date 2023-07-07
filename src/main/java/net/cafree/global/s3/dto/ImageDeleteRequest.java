package net.cafree.global.s3.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ImageDeleteRequest(@NotBlank List<String> fileNames,
								 @NotBlank String domain,
								 @NotBlank Long memberId) {

	@Builder
	public ImageDeleteRequest {
	}

	public List<String> getFileNamesWithPath() {
		return fileNames.stream()
			.map(this::getPath)
			.toList();
	}

	private String getPath(String fileName) {
		return "users/" + memberId + "/" + domain + "/" + fileName;
	}
}
