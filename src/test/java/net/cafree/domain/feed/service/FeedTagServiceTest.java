package net.cafree.domain.feed.service;

import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedTag;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.repository.FeedRepository;
import net.cafree.domain.feed.repository.FeedTagRepository;
import net.cafree.domain.feed.repository.TagRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FeedTagServiceTest {

    @InjectMocks
    FeedTagService feedTagService;

    @Mock
    FeedTagRepository feedTagRepository;

    @Mock
    FeedRepository feedRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    CafeRepository cafeRepository;

    @Mock
    CafeAddressRepository cafeAddressRepository;

    @Mock
    MemberRepository memberRepository;

    @AfterEach
    public void cleanup() {
        feedTagRepository.deleteAll();
    }

    @Test
    @DisplayName("요청 온 피드와 태그들의 관계를 모두 저장한다")
    public void saveAll() {
        // given
        Feed mockFeed = getSavedFeed();
        List<Tag> mockTags = List.of(
                new Tag("안양 카페"),
                new Tag("안양역 카페"),
                new Tag("안양 스타벅스"));
        List<FeedTag> mockFeedTags = List.of(
                new FeedTag(mockTags.get(0), mockFeed),
                new FeedTag(mockTags.get(1), mockFeed),
                new FeedTag(mockTags.get(2), mockFeed));

        given(feedTagRepository.saveAll(any()))
                .willReturn(mockFeedTags);

        // when
        List<FeedTag> savedFeedTags = feedTagService.saveAll(mockFeed, mockTags);

        // then
        assertThat(savedFeedTags.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("id에 해당하는 FeedTag를 리턴한다")
    public void findById() {
        // given
        Feed mockFeed = getSavedFeed();
        String tagName = "안양 스타벅스";
        Tag mockTag = new Tag(tagName);
        FeedTag mockFeedTag = new FeedTag(mockTag, mockFeed);
        Long mockId = 1L;

        ReflectionTestUtils.setField(mockFeedTag, "id", mockId);
        given(feedTagRepository.findById(mockId))
                .willReturn(Optional.of(mockFeedTag));

        // when
        feedTagRepository.save(mockFeedTag);

        // then
        FeedTag findFeedTag = feedTagService.findById(mockId);

        assertThat(findFeedTag.getId()).isEqualTo(mockId);
    }

    @Test
    @DisplayName("피드 id에 해당하는 FeedTag 리스트를 리턴한다")
    public void findByFeedId() {
        // given
        Feed mockFeed = getSavedFeed();
        List<Tag> mockTags = List.of(
                new Tag("안양 카페"),
                new Tag("안양역 카페"),
                new Tag("안양 스타벅스"));
        List<FeedTag> mockFeedTags = List.of(
                new FeedTag(mockTags.get(0), mockFeed),
                new FeedTag(mockTags.get(1), mockFeed),
                new FeedTag(mockTags.get(2), mockFeed));

        Long mockFeedId = 1L;
        given(feedTagRepository.findByFeedId(mockFeedId))
                .willReturn(mockFeedTags);

        // when
        feedTagService.saveAll(mockFeed, mockTags);

        // then
        List<FeedTag> findFeedTags = feedTagService.findByFeedId(mockFeedId);
        assertThat(findFeedTags.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("피드 id에 해당하는 FeedTag 리스트를 성공적으로 삭제하면 예외가 발생하지 않는다")
    public void deleteAllSuccess() {
        // given
        Feed mockFeed = getSavedFeed();
        List<Tag> mockTags = List.of(
                new Tag("안양 카페"),
                new Tag("안양역 카페"),
                new Tag("안양 스타벅스"));
        List<FeedTag> mockFeedTags = List.of(
                new FeedTag(mockTags.get(0), mockFeed),
                new FeedTag(mockTags.get(1), mockFeed),
                new FeedTag(mockTags.get(2), mockFeed));

        Long mockFeedId = 1L;
        given(feedTagRepository.findByFeedId(mockFeedId))
                .willReturn(mockFeedTags);

        // when

        // then
        assertThatNoException()
                .isThrownBy(() -> feedTagService.deleteAll(mockFeedId));
    }

    @Test
    @DisplayName("피드 id에 해당하는 FeedTag 리스트를 성공적으로 삭제하면 예외가 발생한다")
    public void deleteAllFail() {
        // given
        Feed mockFeed = getSavedFeed();
        List<Tag> mockTags = List.of(
                new Tag("안양 카페"),
                new Tag("안양역 카페"),
                new Tag("안양 스타벅스"));
        List<FeedTag> mockFeedTags = List.of(
                new FeedTag(mockTags.get(0), mockFeed),
                new FeedTag(mockTags.get(1), mockFeed),
                new FeedTag(mockTags.get(2), mockFeed));

        Long mockFeedId = 1L;
        given(feedTagRepository.findByFeedId(mockFeedId))
                .willReturn(mockFeedTags);

        // when

        // then
        assertThatThrownBy(() -> feedTagService.deleteAll(2L));
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
