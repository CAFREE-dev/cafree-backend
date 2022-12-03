package net.cafree.domain.feed.dto.request;

import lombok.Builder;

import java.util.List;

public record FeedUpdateRequest(String contents,
                                Double likePoint,
                                List<String> tags,
                                Long cafeId) {

    @Builder
    public FeedUpdateRequest {
    }
}
