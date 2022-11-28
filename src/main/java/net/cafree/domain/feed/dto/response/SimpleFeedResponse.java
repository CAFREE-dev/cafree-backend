package net.cafree.domain.feed.dto.response;

import lombok.Builder;

public record SimpleFeedResponse(Long id,
                                 Long cafeId,
                                 String imageUrl,
                                 Long memberId) {

    @Builder
    public SimpleFeedResponse { }
}
