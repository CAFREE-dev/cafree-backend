package net.cafree.domain.feed.service;

import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.repository.FeedRepository;
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

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @InjectMocks
    TagService tagService;

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
        tagRepository.deleteAll();
    }

    @Test
    @DisplayName("요청 온 피드의 태그들을 모두 저장한다")
    public void saveAll() {
        // given
        Feed feed = getSavedFeed();
        String tag1 = "스타벅스";
        String tag2 = "안양일번가 카페";
        String tag3 = "안양 카페";

        Tag mockTag = new Tag("mock Tag", 0);

        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .tags(List.of(tag1, tag2, tag3))
                .build();

        given(tagRepository.save(any()))
                .willReturn(mockTag);

        // when
        List<Tag> savedTags = tagService.saveAll(feedAddRequest);

        // then
        assertThat(savedTags.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이미 존재하는 태그의 저장을 요청하면 카운트가 올라간다")
    public void saveExistTag() {
        // given
        Feed feed = getSavedFeed();
        String tagName = "스타벅스";
        Tag mockTag = new Tag(tagName, 1);

        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .tags(List.of(tagName))
                .build();

        given(tagRepository.findByTagName(tagName))
                .willReturn(Optional.of(mockTag));

        // when
        List<Tag> savedTags = tagService.saveAll(feedAddRequest);

        // then
        assertThat(savedTags.get(0).getTaggedCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("id에 해당하는 태그를 리턴한다")
    public void findById() {
        // given
        Feed feed = getSavedFeed();
        String tag1 = "스타벅스";
        String tag2 = "안양일번가 카페";
        String tag3 = "안양 카페";

        Tag mockTag = new Tag(tag1, 0);
        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .tags(List.of(tag1, tag2, tag3))
                .build();

        Long mockId = 1L;
        ReflectionTestUtils.setField(mockTag, "id", mockId);

        given(tagRepository.save(any()))
                .willReturn(mockTag);
        given(tagRepository.findById(mockId))
                .willReturn(Optional.of(mockTag));

        // when
        List<Tag> savedTags = tagService.saveAll(feedAddRequest);

        // then
        Tag findTag = tagService.findById(mockId);
        assertThat(findTag.getId()).isEqualTo(mockId);
        assertThat(findTag.getTagName()).isEqualTo(tag1);
    }

    @Test
    @DisplayName("모든 태그를 리턴한다")
    public void findAll() {
        // given
        List<Tag> mockTags = List.of(new Tag("tag1", 0), new Tag("tag2", 0), new Tag("tag3", 0));
        given(tagRepository.findAll())
                .willReturn(mockTags);

        // when
        List<Tag> tags = tagService.findAll();

        // then
        assertThat(tags.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("id 리스트에 포함돼있는 태그 리스트를 리턴한다")
    public void findByIds() {
        // given
        List<Tag> mockTags = List.of(new Tag("tag1", 0), new Tag("tag2", 0), new Tag("tag3", 0));
        List<Long> idList = List.of(1L, 2L);
        given(tagRepository.findByIdIn(idList))
                .willReturn(List.of(mockTags.get(0), mockTags.get(2)));

        // when
        List<Tag> tags = tagService.findByIds(idList);

        // then
        assertThat(tags.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("성공적으로 태그가 삭제되면 예외가 발생하지 않는다")
    public void deleteTagByExistTag() {
        String tagName = "태그이름";
        Tag tag = new Tag(tagName, 0);
        Long mockId = 1L;

        given(tagRepository.findById(mockId))
                .willReturn(Optional.of(tag));

        assertThatNoException()
                .isThrownBy(() -> tagService.delete(tagService.findById(mockId)));
    }

    @Test
    @DisplayName("성공적으로 태그가 삭제되지 않으면 예외가 발생한다")
    public void deleteTagByNotExistTag() {
        Long mockId = 1L;

        assertThatThrownBy(() -> tagService.delete(tagService.findById(mockId)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("태그 카운트를 하나씩 감소시킨다")
    public void downTagCount() {
        List<Tag> mockTags = List.of(new Tag("tag1", 2), new Tag("tag2", 3), new Tag("tag3", 4));
        given(tagRepository.findAll())
                .willReturn(mockTags);

        tagService.downTaggedCountOrDelete(tagService.findAll());

        assertThat(mockTags.get(0).getTaggedCount()).isEqualTo(1);
        assertThat(mockTags.get(1).getTaggedCount()).isEqualTo(2);
        assertThat(mockTags.get(2).getTaggedCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("태그 카운트가 0이되면 삭제시킨다")
    public void deleteTagByZeroTaggedCount() {
        List<Tag> mockTags = List.of(new Tag("tag1", 1), new Tag("tag2", 1));
        given(tagRepository.findAll())
                .willReturn(mockTags);

        tagService.downTaggedCountOrDelete(tagService.findAll());

        verify(tagRepository, times(2)).delete(any());
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
