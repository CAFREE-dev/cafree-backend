package net.cafree.domain.feed.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.member.entity.Member;
import net.cafree.global.BaseTime;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String contents;

    @Embedded
    private BaseTime baseTime;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Feed(String contents, Cafe cafe, Member member) {
        this.contents = contents;
        this.cafe = cafe;
        this.member = member;
    }
}
