package net.cafree.domain.feed.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.cafree.domain.feed.dto.response.SimpleFeedResponse;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer sequence;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public FeedImage(String imageUrl, Feed feed, Integer sequence) {
        this.imageUrl = imageUrl;
        this.feed = feed;
        this.sequence = sequence;
    }

    public SimpleFeedResponse toSimpleFeedResponse(){
        return SimpleFeedResponse.builder()
                .id(feed.getId())
                .cafeId(feed.getCafe().getId())
                .imageUrl(imageUrl)
                .memberId(1L)
                .build();
    }
}
