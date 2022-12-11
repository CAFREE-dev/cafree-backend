package net.cafree.domain.feed.repository;

import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedTag;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.member.entity.Member;
import net.cafree.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FeedTagRepositoryTest {
    @Autowired
    private FeedTagRepository feedTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private CafeAddressRepository cafeAddressRepository;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void cleanup(){
        feedTagRepository.deleteAll();
    }

    @Test
    @DisplayName("피드 태그 등록을 요청하면 새로운 피드 태그가 등록된다")
    public void registerNewCafe(){
        // given
        String tagName = "태그입니다.";
        FeedTag feedTag = feedTagRepository.save(new FeedTag(getSavedTag(tagName), getSavedFeed()));

        // then
        assertThat(feedTag.getTag().getTagName()).isEqualTo(tagName);
    }

    @Test
    @DisplayName("모든 피드 태그를 반환한다")
    public void getAllFeed() {
        // given
        String tagName = "태그입니다.";
        feedTagRepository.save(new FeedTag(getSavedTag(tagName), getSavedFeed()));
        feedTagRepository.save(new FeedTag(getSavedTag(tagName), getSavedFeed()));

        // when
        List<FeedTag> feedTags = feedTagRepository.findAll();

        // then
        assertThat(feedTags).hasSize(2);
    }

    private Tag getSavedTag(String tag) {
        return tagRepository.save(new Tag(tag, 0));
    }

    private Feed getSavedFeed() {
        String contents = "피드입니다.";
        return feedRepository.save(Feed.builder()
                .contents(contents)
                .cafe(getSavedCafe())
                .member(getSavedMember())
                .build());
    }

    private Cafe getSavedCafe() {
        String title = "스타벅스";
        String mapUrl = "https://map.naver.com/v5/search/스타벅스%20안양일번가/place/37169041?placePath=%3Fentry=pll%26from=nx%26fromNxList=true";
        String sido = "경기";
        String sigungu = "안양시 만안구";
        String dong = "안양동";
        String doro = "장내로149번길 53";
        String buildNo = "674-81";
        String branch = "안양역점";
        BigDecimal latitude = BigDecimal.valueOf(126.922145);
        BigDecimal longitude = BigDecimal.valueOf(37.4002960);
        CafeAddress cafeAddress = cafeAddressRepository.save(CafeAddress.builder()
                .sido(sido)
                .sigungu(sigungu)
                .dong(dong)
                .doro(doro)
                .buildNo(buildNo)
                .branch(branch)
                .latitude(latitude)
                .longitude(longitude)
                .build());

        return cafeRepository.save(Cafe.builder()
                .title(title)
                .mapUrl(mapUrl)
                .cafeAddress(cafeAddress)
                .build());
    }

    private Member getSavedMember() {
        String name = "홍길동";
        return memberRepository.save(Member.builder().name(name).build());
    }
}