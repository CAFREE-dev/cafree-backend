package net.cafree.domain.feed.dto.request;

import lombok.Builder;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
import net.cafree.domain.member.entity.Member;
import net.cafree.s3.util.S3File;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public record FeedAddRequest(String contents,
                             Double rating,
                             List<MultipartFile> images,
                             List<Integer> imageSequences,
                             List<String> tags,
                             Long cafeId) {

    @Builder
    public FeedAddRequest { }

    public Feed toFeedEntity(Cafe cafe, Member member) {
        return Feed.builder()
                .contents(contents)
                .rating(rating)
                .cafe(cafe)
                .member(member)
                .build();
    }

    public List<FeedImage> toFeedImageEntity(Feed feed, List<S3File> images) {
        List<FeedImage> feedImages = new ArrayList<>();

        for(int i=0 ; i<imageSequences.size() ; i++) {
            feedImages.add(new FeedImage(
                    images.get(i).getFileName(),
                    images.get(i).getFileUrl(),
                    imageSequences.get(i),
                    feed));
        }

        return feedImages;
    }

    /* 2022.11.28 - lcomment : User 기능 구현 후 수정 필요 */
    public Member toMemberEntity() {
        return null;
    }
}