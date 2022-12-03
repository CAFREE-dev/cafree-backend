package net.cafree.domain.feed.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.feed.dto.response.FeedResponse;
import net.cafree.domain.member.entity.Member;
import net.cafree.global.BaseTime;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String contents;

    @Column
    private Double rating;

    @Embedded
    private BaseTime baseTime;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Feed(String contents, Double rating, Cafe cafe, Member member) {
        this.contents = contents;
        this.rating = rating;
        this.cafe = cafe;
        this.member = member;
    }

    public void updateFeed(String contents, Double rating, Cafe cafe) {
        this.contents = contents;
        this.rating = rating;
        this.cafe = cafe;
    }

    /* 2022.11.29 lcomment : User 기능 구현 후 수정 필요 (isLiked, memberId, memberNickname) */
    public FeedResponse toFeedResponse(List<String> imageUrls, List<Integer> imageSequences, List<String> tags) {
        return FeedResponse.builder()
                .id(id)
                .createdAt(baseTime.getCreatedAt())
                .imageUrls(imageUrls)
                .imageSequences(imageSequences)
                .tags(tags)
                .cafeId(cafe.getId())
                .cafeTitle(cafe.getTitle())
                .cafePreview("카페 간단 소개글입니다")
                .rating(rating)
                .isLiked(false)
                .memberId(1L)
                .memberNickname("test")
                .build();
    }
}
