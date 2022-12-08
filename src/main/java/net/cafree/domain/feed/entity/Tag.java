package net.cafree.domain.feed.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String tagName;

    private Integer taggedCount;

    public Tag(String tagName, Integer taggedCount) {
        this.tagName = tagName;
        this.taggedCount = taggedCount;
    }

    public void addCount(int count) {
        this.taggedCount += count;
    }

    public void subtractCount(int count) {
        this.taggedCount -= count;
    }
}
