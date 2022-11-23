package net.cafree.domain.cafe.service;

import net.cafree.domain.cafe.dto.request.CafeRegisterRequest;
import net.cafree.domain.cafe.dto.request.CafeUpdateRequest;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
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
class CafeServiceTest {

    @InjectMocks
    CafeService cafeService;
    @Mock
    CafeRepository cafeRepository;
    @Mock
    CafeAddressRepository cafeAddressRepository;

    @AfterEach
    public void cleanup() {
        cafeRepository.deleteAll();
    }

    @Test
    @DisplayName("id를 넣으면 해당 id의 카페가 반환된다")
    public void findCafeById() {
        // given
        String title = "모노폴리로그";
        String mapUrl = "url1";
        CafeRegisterRequest cafeRegisterRequest = CafeRegisterRequest.builder().build();
        CafeAddress mockCafeAddress = CafeAddress.builder().build();
        Cafe mockCafe = Cafe.builder()
                .title(title)
                .likeCount(0)
                .mapUrl(mapUrl)
                .cafeAddress(mockCafeAddress)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockCafe, "id", mockId);

        given(cafeRepository.save(any()))
                .willReturn(mockCafe);
        given(cafeRepository.findById(mockId))
                .willReturn(Optional.of(mockCafe));

        // when
        cafeService.save(cafeRegisterRequest);

        // then
        Cafe cafe = cafeService.findById(mockId);

        assertThat(cafe.getId()).isEqualTo(mockId);
        assertThat(cafe.getTitle()).isEqualTo(title);
        assertThat(cafe.getMapUrl()).isEqualTo(mapUrl);
    }

    @Test
    @DisplayName("카페 등록 request를 넣으면 등록된 카페가 반환된다")
    public void saveCafe() {
        // given
        String title = "스타벅스";
        String mapUrl = "url1";
        CafeRegisterRequest cafeRegisterRequest = CafeRegisterRequest.builder().build();
        CafeAddress mockCafeAddress = CafeAddress.builder().build();
        Cafe mockCafe = Cafe.builder()
                .title(title)
                .likeCount(0)
                .mapUrl(mapUrl)
                .cafeAddress(mockCafeAddress)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockCafe, "id", mockId);

        given(cafeRepository.save(any()))
                .willReturn(mockCafe);

        // when
        Cafe cafe = cafeService.save(cafeRegisterRequest);

        // then
        assertThat(cafe.getId()).isEqualTo(mockId);
        assertThat(cafe.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("카페 목록이 반환된다")
    public void findAllCafe() {
        // given
        List<Cafe> mockCafes = List.of(Cafe.builder().build(), Cafe.builder().build(), Cafe.builder().build(), Cafe.builder().build());
        given(cafeRepository.findAll())
                .willReturn(mockCafes);
        // when
        List<Cafe> cafeList = cafeService.findAll();

        // then
        assertThat(cafeList.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("업데이트된 카페가 반환된다")
    public void updateCafe() {
        // given
        String updatedTitle = "수정된 제목";
        Integer updatedLikeCount = 1;
        String updatedMapUrl = "수정된 주소";
        String title = "모노폴리로그";
        String mapUrl = "url1";
        String sido = "경기";
        String sigungu = "안양시 만안구";
        String dong = "안양동";
        String doro = "장내로149번길 53";
        String buildNo = "674-81";
        String branch = "안양역점";
        BigDecimal latitude = BigDecimal.valueOf(126.922145);
        BigDecimal longitude = BigDecimal.valueOf(37.4002960);

        CafeUpdateRequest cafeUpdateRequest = CafeUpdateRequest.builder()
                .title(updatedTitle)
                .likeCount(updatedLikeCount)
                .mapUrl(updatedMapUrl)
                .build();
        CafeAddress mockCafeAddress = CafeAddress.builder()
                .build();
        Cafe mockCafe = Cafe.builder()
                .title(title)
                .likeCount(0)
                .mapUrl(mapUrl)
                .cafeAddress(mockCafeAddress)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockCafe, "id", mockId);

        given(cafeRepository.findById(mockId))
                .willReturn(Optional.of(mockCafe));

        // when
        Cafe updatedCafe = cafeService.update(mockId, cafeUpdateRequest);

        // then
        assertThat(updatedCafe.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedCafe.getLikeCount()).isEqualTo(updatedLikeCount);
        assertThat(updatedCafe.getMapUrl()).isEqualTo(updatedMapUrl);
    }

    @Test
    @DisplayName("성공적으로 삭제되면 예외가 발생하지 않는다")
    public void deleteCafeByExistCafe() {
        String title = "모노폴리로그";
        String mapUrl = "url1";
        Cafe mockCafe = Cafe.builder()
                .title(title)
                .likeCount(0)
                .mapUrl(mapUrl)
                .cafeAddress(null)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockCafe, "id", mockId);

        given(cafeRepository.findById(mockId))
                .willReturn(Optional.of(mockCafe));

        assertThatNoException()
                .isThrownBy(() -> cafeService.delete(mockId));
    }

    @Test
    @DisplayName("성공적으로 삭제되지 않으면 예외가 발생한다")
    public void deleteCafeByNotExistCafe() {
        String title = "모노폴리로그";
        String mapUrl = "url1";
        Cafe mockCafe = Cafe.builder()
                .title(title)
                .likeCount(0)
                .mapUrl(mapUrl)
                .cafeAddress(null)
                .build();
        Long mockId = 1L;
        ReflectionTestUtils.setField(mockCafe, "id", mockId);

        given(cafeRepository.findById(mockId))
                .willReturn(Optional.of(mockCafe));

        assertThatThrownBy(() -> cafeService.delete(2L));
    }
}