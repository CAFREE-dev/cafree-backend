package net.cafree.domain.feed.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record FeedResponse(Long id,
                           LocalDateTime createdAt,
                           List<String> imageUrls,
                           List<String> tags,
                           Long cafeId,
                           String cafeTitle,
                           String cafePreview,
                           Long likeCount,
                           Boolean isLiked,
                           Long memberId,
                           String memberNickname) {

    @Builder
    public FeedResponse { }
}
