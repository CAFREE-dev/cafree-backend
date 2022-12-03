package net.cafree.domain.feed.repository;

import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
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
class FeedImageRepositoryTest {
    @Autowired
    private FeedImageRepository feedImageRepository;
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
        feedImageRepository.deleteAll();
    }

    @Test
    @DisplayName("피드 이미지 등록을 요청하면 새로운 피드 이미지가 등록된다")
    public void registerNewCafe(){
        // given
        Integer seq = 1;
        String imageUrl = "https://image.cafree.net/v1/12345678";
        FeedImage feedImage = feedImageRepository.save(new FeedImage(imageUrl, getSavedFeed(), seq));

        // then
        assertThat(feedImage.getImageUrl()).isEqualTo(imageUrl);
        assertThat(feedImage.getSequence()).isEqualTo(seq);
    }

    @Test
    @DisplayName("모든 피드 이미지를 반환한다")
    public void getAllFeed() {
        // given
        String imageUrl = "https://image.cafree.net/v1/12345678";
        feedImageRepository.save(new FeedImage(imageUrl, getSavedFeed(), 1));
        feedImageRepository.save(new FeedImage(imageUrl, getSavedFeed(), 2));

        // when
        List<FeedImage> feedImages = feedImageRepository.findAll();

        // then
        assertThat(feedImages).hasSize(2);
    }

    private Feed getSavedFeed() {
        String contents = "피드입니다.";
        return feedRepository.save(Feed.builder()
                .contents(contents)
                .cafe(getSavedCafe())
                .rating(2.5)
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
        String build_no = "674-81";
        String branch = "안양역점";
        BigDecimal latitude = BigDecimal.valueOf(126.922145);
        BigDecimal longitude = BigDecimal.valueOf(37.4002960);
        CafeAddress cafeAddress = cafeAddressRepository.save(CafeAddress.builder()
                .sido(sido)
                .sigungu(sigungu)
                .dong(dong)
                .doro(doro)
                .buildNo(build_no)
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