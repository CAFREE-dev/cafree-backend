package net.cafree.domain.feed.dto.request;

import lombok.Builder;

import java.util.List;

public record FeedAddRequest(String contents,
                             Double likePoint,
                             List<String> imageUrls,
                             List<String> tags,
                             Long cafeId) {

    @Builder
    public FeedAddRequest { }
}