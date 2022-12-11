package net.cafree.domain.feed.service;

import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.dto.request.FeedUpdateRequest;
import net.cafree.domain.feed.entity.Feed;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {

    @InjectMocks
    FeedService feedService;

    @Mock
    FeedRepository feedRepository;

    @Mock
    CafeRepository cafeRepository;

    @Mock
    CafeAddressRepository cafeAddressRepository;

    @Mock
    MemberRepository memberRepository;

    @AfterEach
    public void cleanup(){
        feedRepository.deleteAll();
    }

    @Test
    @DisplayName("id를 넣으면 해당 id의 피드 반환")
    public void findFeedById() {
        // given
        String contents = "test contents";
        Double rating = 2.5;
        Cafe mockCafe = getSavedCafe();
        Member mockMember = getSavedMember();
        FeedAddRequest feedAddRequest = FeedAddRequest.builder().build();
        Feed mockFeed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .member(mockMember)
                .cafe(mockCafe)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockFeed, "id", mockId);

        given(feedRepository.save(any()))
                .willReturn(mockFeed);
        given(feedRepository.findById(mockId))
                .willReturn(Optional.of(mockFeed));

        // when
        feedService.save(feedAddRequest, mockCafe, mockMember);

        // then
        Feed feed = feedService.findById(mockId);

        assertThat(feed.getId()).isEqualTo(mockId);
        assertThat(feed.getContents()).isEqualTo(contents);
        assertThat(feed.getRating()).isEqualTo(rating);
    }

    @Test
    @DisplayName("피드를 등록하면 등록된 피드가 반환된다")
    public void saveFeed(){
        // given
        String contents = "test contents";
        Double rating = 2.5;
        Cafe mockCafe = getSavedCafe();
        Member mockMember = getSavedMember();
        FeedAddRequest feedAddRequest = FeedAddRequest.builder().build();
        Feed mockFeed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .member(mockMember)
                .cafe(mockCafe)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockFeed, "id", mockId);

        given(feedRepository.save(any()))
                .willReturn(mockFeed);

        // when
        Feed feed = feedService.save(feedAddRequest, mockCafe, mockMember);

        // then
        assertThat(feed.getId()).isEqualTo(mockId);
        assertThat(feed.getContents()).isEqualTo(contents);
        assertThat(feed.getRating()).isEqualTo(rating);
    }

    @Test
    @DisplayName("업데이트 된 피드가 반환된다")
    public void updateFeed(){
        // given
        String updatedContents = "update test contents";
        Double updatedRating = 4.5;

        String contents = "test contents";
        Double rating = 2.5;
        Cafe mockCafe = getSavedCafe();
        Member mockMember = getSavedMember();

        FeedUpdateRequest feedUpdateRequest = FeedUpdateRequest.builder()
                .contents(updatedContents)
                .cafeId(1L)
                .rating(updatedRating)
                .build();
        Feed mockFeed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .member(mockMember)
                .cafe(mockCafe)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockFeed, "id", mockId);

        given(feedRepository.findById(mockId))
                .willReturn(Optional.of(mockFeed));

        // when
        Feed feed = feedService.update(mockId, feedUpdateRequest, mockCafe);

        // then
        assertThat(feed.getId()).isEqualTo(mockId);
        assertThat(feed.getContents()).isEqualTo(updatedContents);
        assertThat(feed.getRating()).isEqualTo(updatedRating);
    }

    @Test
    @DisplayName("성공적으로 삭제되면 예외가 발생하지 않는다")
    public void deleteFeedByIdSuccess(){
        // given
        String contents = "test contents";
        Double rating = 2.5;
        Cafe mockCafe = getSavedCafe();
        Member mockMember = getSavedMember();

        Feed mockFeed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .member(mockMember)
                .cafe(mockCafe)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockFeed, "id", mockId);

        given(feedRepository.findById(mockId))
                .willReturn(Optional.of(mockFeed));

        // then
        assertThatNoException()
                .isThrownBy(() -> feedService.delete(feedService.findById(mockId)));
    }

    @Test
    @DisplayName("성공적으로 삭제되지 않으면 예외가 발생한다")
    public void deleteFeedByIdFail(){
        // given
        String contents = "test contents";
        Double rating = 2.5;
        Cafe mockCafe = getSavedCafe();
        Member mockMember = getSavedMember();

        Feed mockFeed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .member(mockMember)
                .cafe(mockCafe)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockFeed, "id", mockId);

        given(feedRepository.findById(mockId))
                .willReturn(Optional.of(mockFeed));

        // then
        assertThatThrownBy(() -> feedService.delete(feedService.findById(2L)));
    }

    private Member getSavedMember() {
        String name = "홍길동";
        return memberRepository.save(Member.builder().name(name).build());
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
}
