package net.cafree.domain.feed.service;

import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
import net.cafree.domain.feed.repository.FeedImageRepository;
import net.cafree.domain.feed.repository.FeedRepository;
import net.cafree.domain.member.entity.Member;
import net.cafree.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FeedImageServiceTest {

    @InjectMocks
    FeedImageService feedImageService;

    @Mock
    FeedImageRepository feedImageRepository;

    @Mock
    FeedRepository feedRepository;

    @Mock
    CafeRepository cafeRepository;

    @Mock
    CafeAddressRepository cafeAddressRepository;

    @Mock
    MemberRepository memberRepository;

    @AfterEach
    public void cleanup() {
        feedImageRepository.deleteAll();
    }

    @Test
    @DisplayName("요청 온 이미지를 모두 저장한다")
    public void saveAll() {
        // given
        Feed feed = getSavedFeed();
        String imageUrl1 = "https://image.cafree.net/v1/imageUrl1";
        String imageUrl2 = "https://image.cafree.net/v1/imageUrl2";
        String imageUrl3 = "https://image.cafree.net/v1/imageUrl3";

        List<FeedImage> mockFeedImages = List.of(
                new FeedImage(imageUrl1, feed, 1),
                new FeedImage(imageUrl2, feed, 2),
                new FeedImage(imageUrl3, feed, 3));
        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .imageUrls(List.of(imageUrl1, imageUrl2, imageUrl3))
                .imageSequences(List.of(1, 2, 3))
                .build();

        given(feedImageRepository.saveAll(any()))
                .willReturn(mockFeedImages);
        given(feedImageRepository.findByFeed(feed))
                .willReturn(mockFeedImages);

        // when
        List<FeedImage> saveFeedImages = feedImageService.saveAll(feedAddRequest, feed);

        // then
        List<FeedImage> findFeedImages = feedImageService.findByFeed(feed);

        assertThat(saveFeedImages.size()).isEqualTo(findFeedImages.size());
    }

    @Test
    @DisplayName("해당 피드의 이미지 url 리스트를 리턴한다")
    public void findByFeed() {
        // given
        Feed feed = getSavedFeed();
        String imageUrl1 = "https://image.cafree.net/v1/imageUrl1";
        String imageUrl2 = "https://image.cafree.net/v1/imageUrl2";
        String imageUrl3 = "https://image.cafree.net/v1/imageUrl3";

        List<FeedImage> mockFeedImages = List.of(
                new FeedImage(imageUrl1, feed, 1),
                new FeedImage(imageUrl2, feed, 2),
                new FeedImage(imageUrl3, feed, 3));
        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .imageUrls(List.of(imageUrl1, imageUrl2, imageUrl3))
                .imageSequences(List.of(1, 2, 3))
                .build();

        given(feedImageRepository.findByFeed(feed))
                .willReturn(mockFeedImages);

        // when
        List<FeedImage> saveFeedImages = feedImageService.saveAll(feedAddRequest, feed);

        // then
        List<FeedImage> findFeedImages = feedImageService.findByFeed(feed);

        assertThat(findFeedImages.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 피드의 id로 이미지 url 리스트를 가져온다")
    public void findByFeedId() {
        // given
        Feed mockFeed = getSavedFeed();
        String imageUrl1 = "https://image.cafree.net/v1/imageUrl1";
        String imageUrl2 = "https://image.cafree.net/v1/imageUrl2";
        String imageUrl3 = "https://image.cafree.net/v1/imageUrl3";

        List<FeedImage> mockFeedImages = List.of(
                new FeedImage(imageUrl1, mockFeed, 1),
                new FeedImage(imageUrl2, mockFeed, 2),
                new FeedImage(imageUrl3, mockFeed, 3));
        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .imageUrls(List.of(imageUrl1, imageUrl2, imageUrl3))
                .imageSequences(List.of(1, 2, 3))
                .build();

        given(feedImageRepository.findByFeedId(any()))
                .willReturn(mockFeedImages);

        // when
        List<FeedImage> saveFeedImages = feedImageService.saveAll(feedAddRequest, mockFeed);

        // then
        List<FeedImage> findFeedImages = feedImageService.findByFeedId(1L);

        assertThat(findFeedImages.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("피드의 첫번째 이미지들를 반환한다")
    public void findFirstImage() {
        // given
        Feed feed = getSavedFeed();
        String imageUrl1 = "https://image.cafree.net/v1/imageUrl1";
        String imageUrl2 = "https://image.cafree.net/v1/imageUrl2";
        String imageUrl3 = "https://image.cafree.net/v1/imageUrl3";

        List<FeedImage> mockFeedImages = List.of(
                new FeedImage(imageUrl1, feed, 1),
                new FeedImage(imageUrl2, feed, 2),
                new FeedImage(imageUrl3, feed, 3));
        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .imageUrls(List.of(imageUrl1, imageUrl2, imageUrl3))
                .imageSequences(List.of(1, 2, 3))
                .build();

        given(feedImageRepository.saveAll(any()))
                .willReturn(mockFeedImages);
        given(feedImageRepository.findBySequence(1))
                .willReturn(List.of(mockFeedImages.get(0)));

        // when
        feedImageService.saveAll(feedAddRequest, feed);

        // then
        List<FeedImage> findFeedImages = feedImageService.findFirstImage();

        assertThat(findFeedImages.size()).isEqualTo(1);
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
