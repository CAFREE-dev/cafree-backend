package net.cafree.domain.feed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.service.CafeService;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
import net.cafree.domain.feed.entity.FeedTag;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.service.FeedImageService;
import net.cafree.domain.feed.service.FeedService;
import net.cafree.domain.feed.service.FeedTagService;
import net.cafree.domain.feed.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static net.cafree.util.ApiDocumentUtils.getDocumentRequest;
import static net.cafree.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FeedController.class)
@AutoConfigureRestDocs
public class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeedService feedService;

    @MockBean
    private FeedImageService feedImageService;

    @MockBean
    private TagService tagService;

    @MockBean
    private FeedTagService feedTagService;

    @MockBean
    private CafeService cafeService;

    @Test
    @DisplayName("피드를 등록한다")
    public void feedAdd() throws Exception {
        // given
        Long cafeId = 1L;
        Cafe cafe = getCafe();
        String contents = "매장이 깔끔하고 좋네요";
        Double rating = 4.5;
        List<String> imageUrls = List.of(
                "https://image.cafree.net/v1/feed/1/1.png",
                "https://image.cafree.net/v1/feed/1/2.png",
                "https://image.cafree.net/v1/feed/1/3.png");
        List<Integer> imageSequences = List.of(1, 2, 3);
        List<String> tags = List.of("안양 카페", "안양역 카페", "안양 스타벅스");

        Feed feed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .cafe(cafe)
                .member(null)
                .build();
        Long mockId = 1L;

        List<FeedImage> mockFeedImages = List.of(
                new FeedImage(imageUrls.get(0), feed, 1),
                new FeedImage(imageUrls.get(1), feed, 2),
                new FeedImage(imageUrls.get(2), feed, 3));
        List<Tag> mockTags = List.of(
                new Tag(tags.get(0)),
                new Tag(tags.get(1)),
                new Tag(tags.get(2)));

        ReflectionTestUtils.setField(cafe, "id", cafeId);
        ReflectionTestUtils.setField(feed, "id", mockId);

        given((cafeService.findById(any(Long.class))))
                .willReturn(cafe);
        given(feedService.save(any(FeedAddRequest.class), any(Cafe.class), any()))
                .willReturn(feed);
        given(feedImageService.saveAll(any(FeedAddRequest.class), any(Feed.class)))
                .willReturn(mockFeedImages);
        given(tagService.saveAll(any(FeedAddRequest.class)))
                .willReturn(mockTags);

        // when
        FeedAddRequest feedAddRequest = FeedAddRequest.builder()
                .contents(contents)
                .rating(rating)
                .imageUrls(imageUrls)
                .imageSequences(imageSequences)
                .tags(tags)
                .cafeId(cafeId)
                .build();

        ResultActions result = this.mockMvc.perform(
                post("/api/v1/feeds")
                        .content(objectMapper.writeValueAsString(feedAddRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("feed-add",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("contents").type(JsonFieldType.STRING).description("피드 내용"),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점"),
                                fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("이미지 URL"),
                                fieldWithPath("imageSequences").type(JsonFieldType.ARRAY).description("이미지 순서"),
                                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그"),
                                fieldWithPath("cafeId").type(JsonFieldType.NUMBER).description("카페 ID")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("피드 ID"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성 날짜").optional(),
                                fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("이미지 URL"),
                                fieldWithPath("imageSequences").type(JsonFieldType.ARRAY).description("이미지 순서"),
                                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그"),
                                fieldWithPath("cafeId").type(JsonFieldType.NUMBER).description("카페 ID"),
                                fieldWithPath("cafeTitle").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("cafePreview").type(JsonFieldType.STRING).description("카페 소개글"),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("카페 평점"),
                                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("memberNickname").type(JsonFieldType.STRING).description("유저 닉네임"))));
    }

    @Test
    @DisplayName("특정 id의 카페를 조회한다")
    public void feedDetails() throws Exception {
        // given
        Long cafeId = 1L;
        Cafe cafe = getCafe();
        String contents = "매장이 깔끔하고 좋네요";
        Double rating = 4.5;
        List<String> imageUrls = List.of(
                "https://image.cafree.net/v1/feed/1/1.png",
                "https://image.cafree.net/v1/feed/1/2.png",
                "https://image.cafree.net/v1/feed/1/3.png");
        List<Integer> imageSequences = List.of(1, 2, 3);
        List<String> tags = List.of("안양 카페", "안양역 카페", "안양 스타벅스");

        Feed feed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .cafe(cafe)
                .member(null)
                .build();
        Long mockId = 1L;

        List<FeedImage> mockFeedImages = List.of(
                new FeedImage(imageUrls.get(0), feed, 1),
                new FeedImage(imageUrls.get(1), feed, 2),
                new FeedImage(imageUrls.get(2), feed, 3));
        List<Tag> mockTags = List.of(
                new Tag(tags.get(0)),
                new Tag(tags.get(1)),
                new Tag(tags.get(2)));
        List<FeedTag> mockFeedTags = List.of(
                new FeedTag(mockTags.get(0), feed),
                new FeedTag(mockTags.get(1), feed),
                new FeedTag(mockTags.get(2), feed));

        ReflectionTestUtils.setField(cafe, "id", cafeId);
        ReflectionTestUtils.setField(feed, "id", mockId);

        given((cafeService.findById(any(Long.class))))
                .willReturn(cafe);
        given(feedService.findById(any(Long.class)))
                .willReturn(feed);
        given(feedImageService.findByFeedId(any(Long.class)))
                .willReturn(mockFeedImages);
        given(feedTagService.findByFeedId(any(Long.class)))
                .willReturn(mockFeedTags);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/v1/feeds/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("feed-details",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("식별자")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("피드 ID"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성 날짜").optional(),
                                fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("이미지 URL"),
                                fieldWithPath("imageSequences").type(JsonFieldType.ARRAY).description("이미지 순서"),
                                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그"),
                                fieldWithPath("cafeId").type(JsonFieldType.NUMBER).description("카페 ID"),
                                fieldWithPath("cafeTitle").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("cafePreview").type(JsonFieldType.STRING).description("카페 소개글"),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("카페 평점"),
                                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("memberNickname").type(JsonFieldType.STRING).description("유저 닉네임"))));
    }

    @Test
    @DisplayName("모든 피드를 조회한다")
    public void feedList() throws Exception {
        // given
        Long cafeId = 1L;
        Cafe cafe = getCafe();
        String contents = "매장이 깔끔하고 좋네요";
        Double rating = 4.5;
        List<String> imageUrls = List.of(
                "https://image.cafree.net/v1/feed/1/1.png",
                "https://image.cafree.net/v1/feed/1/2.png",
                "https://image.cafree.net/v1/feed/1/3.png");
        List<Integer> imageSequences = List.of(1, 2, 3);
        List<String> tags = List.of("안양 카페", "안양역 카페", "안양 스타벅스");

        Feed feed = Feed.builder()
                .contents(contents)
                .rating(rating)
                .cafe(cafe)
                .member(null)
                .build();
        Long mockId1 = 1L;

        List<FeedImage> mockFeedImages = List.of(
                new FeedImage(imageUrls.get(0), feed, 1),
                new FeedImage(imageUrls.get(1), feed, 2),
                new FeedImage(imageUrls.get(2), feed, 3));
        List<Tag> mockTags = List.of(
                new Tag(tags.get(0)),
                new Tag(tags.get(1)),
                new Tag(tags.get(2)));
        List<FeedTag> mockFeedTags = List.of(
                new FeedTag(mockTags.get(0), feed),
                new FeedTag(mockTags.get(1), feed),
                new FeedTag(mockTags.get(2), feed));

        String contents2 = "매장이 깔끔하고 좋네요";
        Double rating2 = 3.5;
        List<String> imageUrls2 = List.of(
                "https://image.cafree.net/v1/feed/2/1.png",
                "https://image.cafree.net/v1/feed/2/2.png",
                "https://image.cafree.net/v1/feed/2/3.png");
        List<Integer> imageSequences2 = List.of(1, 2, 3);
        List<String> tags2 = List.of("안양 카페", "안양역 카페", "안양 스타벅스");

        Feed feed2 = Feed.builder()
                .contents(contents2)
                .rating(rating2)
                .cafe(cafe)
                .member(null)
                .build();
        Long mockId2 = 2L;

        List<FeedImage> mockFeedImages2 = List.of(
                new FeedImage(imageUrls2.get(0), feed2, 1),
                new FeedImage(imageUrls2.get(1), feed2, 2),
                new FeedImage(imageUrls2.get(2), feed2, 3));
        List<Tag> mockTags2 = List.of(
                new Tag(tags.get(0)),
                new Tag(tags.get(1)),
                new Tag(tags.get(2)));
        List<FeedTag> mockFeedTags2 = List.of(
                new FeedTag(mockTags.get(0), feed2),
                new FeedTag(mockTags.get(1), feed2),
                new FeedTag(mockTags.get(2), feed2));

        ReflectionTestUtils.setField(cafe, "id", cafeId);
        ReflectionTestUtils.setField(feed, "id", mockId1);
        ReflectionTestUtils.setField(feed2, "id", mockId2);

        given(feedImageService.findFirstImage())
                .willReturn(List.of(mockFeedImages.get(0), mockFeedImages2.get(0)));

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/v1/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("feed-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("피드 ID"),
                                fieldWithPath("[].cafeId").type(JsonFieldType.NUMBER).description("카페 ID"),
                                fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("유저 ID"))));
    }

//    @Test
//    @DisplayName("특정 id의 피드를 수정한다")
//    public void feedModify() {
//
//    }

//    @Test
//    @DisplayName("특정 id의 피드를 삭제한다")
//    public void feedRemove() {
//
//    }

    private Cafe getCafe() {
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

        return Cafe.builder()
                .title(title)
                .mapUrl(mapUrl)
                .likeCount(0)
                .cafeAddress(CafeAddress.builder()
                        .sido(sido)
                        .sigungu(sigungu)
                        .eupmyun("")
                        .dong(dong)
                        .doro(doro)
                        .buildNo(buildNo)
                        .branch(branch)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build())
                .build();
    }
}
