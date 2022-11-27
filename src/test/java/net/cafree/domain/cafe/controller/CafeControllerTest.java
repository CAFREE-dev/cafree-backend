package net.cafree.domain.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cafree.domain.cafe.dto.request.CafeRegisterRequest;
import net.cafree.domain.cafe.dto.request.CafeUpdateRequest;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.service.CafeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CafeController.class)
@AutoConfigureRestDocs
class CafeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CafeService cafeService;

    @DisplayName("카페를 등록한다")
    @Test
    public void cafeAdd() throws Exception {
        // given
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

        Cafe cafe = Cafe.builder()
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
        ReflectionTestUtils.setField(cafe, "id", 1L);

        given(cafeService.save(any(CafeRegisterRequest.class)))
                .willReturn(cafe);

        // when
        CafeRegisterRequest cafeRegisterRequest = CafeRegisterRequest.builder()
                .title(title)
                .sido(sido)
                .sigungu(sigungu)
                .eupmyun("")
                .dong(dong)
                .doro(doro)
                .buildNo(buildNo)
                .branch(branch)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        ResultActions result = this.mockMvc.perform(
                post("/api/v1/cafes")
                        .content(objectMapper.writeValueAsString(cafeRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("cafe-add",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("sido").type(JsonFieldType.STRING).description("시도").optional(),
                                fieldWithPath("sigungu").type(JsonFieldType.STRING).description("시군구").optional(),
                                fieldWithPath("eupmyun").type(JsonFieldType.STRING).description("읍면").optional(),
                                fieldWithPath("dong").type(JsonFieldType.STRING).description("동").optional(),
                                fieldWithPath("doro").type(JsonFieldType.STRING).description("도로명").optional(),
                                fieldWithPath("buildNo").type(JsonFieldType.STRING).description("건물번호").optional(),
                                fieldWithPath("branch").type(JsonFieldType.STRING).description("지점").optional(),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("식별자"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("mapUrl").type(JsonFieldType.STRING).description("웹지도주소"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("preview").type(JsonFieldType.STRING).description("간단소개"),
                                fieldWithPath("isMarked").type(JsonFieldType.BOOLEAN).description("스크랩여부"),
                                fieldWithPath("sido").type(JsonFieldType.STRING).description("시도").optional(),
                                fieldWithPath("sigungu").type(JsonFieldType.STRING).description("시군구").optional(),
                                fieldWithPath("eupmyun").type(JsonFieldType.STRING).description("읍면").optional(),
                                fieldWithPath("dong").type(JsonFieldType.STRING).description("동").optional(),
                                fieldWithPath("doro").type(JsonFieldType.STRING).description("도로명").optional(),
                                fieldWithPath("buildNo").type(JsonFieldType.STRING).description("건물번호").optional(),
                                fieldWithPath("branch").type(JsonFieldType.STRING).description("지점").optional(),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"))));
    }

    @DisplayName("특정 id의 카페를 조회한다")
    @Test
    public void cafeDetails() throws Exception {
        // given
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

        Cafe cafe = Cafe.builder()
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
        Long mockId = 1L;
        ReflectionTestUtils.setField(cafe, "id", mockId);

        given(cafeService.findById(any(Long.class)))
                .willReturn(cafe);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/v1/cafes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("cafe-details",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("식별자"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("mapUrl").type(JsonFieldType.STRING).description("웹지도주소"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("preview").type(JsonFieldType.STRING).description("간단소개"),
                                fieldWithPath("isMarked").type(JsonFieldType.BOOLEAN).description("스크랩여부"),
                                fieldWithPath("sido").type(JsonFieldType.STRING).description("시도").optional(),
                                fieldWithPath("sigungu").type(JsonFieldType.STRING).description("시군구").optional(),
                                fieldWithPath("eupmyun").type(JsonFieldType.STRING).description("읍면").optional(),
                                fieldWithPath("dong").type(JsonFieldType.STRING).description("동").optional(),
                                fieldWithPath("doro").type(JsonFieldType.STRING).description("도로명").optional(),
                                fieldWithPath("buildNo").type(JsonFieldType.STRING).description("건물번호").optional(),
                                fieldWithPath("branch").type(JsonFieldType.STRING).description("지점").optional(),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"))));
    }

    @DisplayName("모든 카페를 조회한다")
    @Test
    public void cafeList() throws Exception {
        // given
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

        Cafe cafe = Cafe.builder()
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
        Long mockId = 1L;
        ReflectionTestUtils.setField(cafe, "id", mockId);

        given(cafeService.findAll())
                .willReturn(List.of(cafe));

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/v1/cafes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("cafe-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("식별자"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("[].mapUrl").type(JsonFieldType.STRING).description("웹지도주소"),
                                fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("[].preview").type(JsonFieldType.STRING).description("간단소개"),
                                fieldWithPath("[].isMarked").type(JsonFieldType.BOOLEAN).description("스크랩여부"),
                                fieldWithPath("[].sido").type(JsonFieldType.STRING).description("시도").optional(),
                                fieldWithPath("[].sigungu").type(JsonFieldType.STRING).description("시군구").optional(),
                                fieldWithPath("[].eupmyun").type(JsonFieldType.STRING).description("읍면").optional(),
                                fieldWithPath("[].dong").type(JsonFieldType.STRING).description("동").optional(),
                                fieldWithPath("[].doro").type(JsonFieldType.STRING).description("도로명").optional(),
                                fieldWithPath("[].buildNo").type(JsonFieldType.STRING).description("건물번호").optional(),
                                fieldWithPath("[].branch").type(JsonFieldType.STRING).description("지점").optional(),
                                fieldWithPath("[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("[].longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("[].distance").type(JsonFieldType.NUMBER).description("거리"))));
    }

    @DisplayName("특정 id의 카페를 수정한다")
    @Test
    public void cafeModify() throws Exception {
        // given
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

        Cafe cafe = Cafe.builder()
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
        Long mockId = 1L;
        ReflectionTestUtils.setField(cafe, "id", mockId);

        given(cafeService.update(any(Long.class), any(CafeUpdateRequest.class)))
                .willReturn(cafe);

        // when
        CafeUpdateRequest cafeUpdateRequest = CafeUpdateRequest.builder()
                .title(title)
                .likeCount(0)
                .mapUrl(mapUrl)
                .sido(sido)
                .sigungu(sigungu)
                .eupmyun("")
                .dong(dong)
                .doro(doro)
                .buildNo(buildNo)
                .branch(branch)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        ResultActions result = this.mockMvc.perform(
                put("/api/v1/cafes/{id}", mockId)
                        .content(objectMapper.writeValueAsString(cafeUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("cafe-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("식별자")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("mapUrl").type(JsonFieldType.STRING).description("웹지도주소"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("sido").type(JsonFieldType.STRING).description("시도").optional(),
                                fieldWithPath("sigungu").type(JsonFieldType.STRING).description("시군구").optional(),
                                fieldWithPath("eupmyun").type(JsonFieldType.STRING).description("읍면").optional(),
                                fieldWithPath("dong").type(JsonFieldType.STRING).description("동").optional(),
                                fieldWithPath("doro").type(JsonFieldType.STRING).description("도로명").optional(),
                                fieldWithPath("buildNo").type(JsonFieldType.STRING).description("건물번호").optional(),
                                fieldWithPath("branch").type(JsonFieldType.STRING).description("지점").optional(),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("식별자"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("카페명"),
                                fieldWithPath("mapUrl").type(JsonFieldType.STRING).description("웹지도주소"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("preview").type(JsonFieldType.STRING).description("간단소개"),
                                fieldWithPath("isMarked").type(JsonFieldType.BOOLEAN).description("스크랩여부"),
                                fieldWithPath("sido").type(JsonFieldType.STRING).description("시도").optional(),
                                fieldWithPath("sigungu").type(JsonFieldType.STRING).description("시군구").optional(),
                                fieldWithPath("eupmyun").type(JsonFieldType.STRING).description("읍면").optional(),
                                fieldWithPath("dong").type(JsonFieldType.STRING).description("동").optional(),
                                fieldWithPath("doro").type(JsonFieldType.STRING).description("도로명").optional(),
                                fieldWithPath("buildNo").type(JsonFieldType.STRING).description("건물번호").optional(),
                                fieldWithPath("branch").type(JsonFieldType.STRING).description("지점").optional(),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"))));
    }

    @DisplayName("특정 id의 카페를 삭제한다")
    @Test
    public void cafeRemove() throws Exception {
        // given
        Long mockId = 1L;

        // when
        ResultActions result = this.mockMvc.perform(
                delete("/api/v1/cafes/{id}", mockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("cafe-remove",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("식별자")
                        )));
    }
}