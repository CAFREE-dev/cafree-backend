package net.cafree.domain.cafe.dao;

import net.cafree.domain.cafe.domain.Cafe;
import net.cafree.domain.cafe.domain.CafeAddress;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CafeRepositoryTest {
    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    CafeAddressRepository cafeAddressRepository;

    @BeforeEach
    void setup(){
        CafeAddress cafeAddress1 = CafeAddress.builder()
                .sido("경기")
                .sigungu("안양시 만안구")
                .dong("안양동")
                .doro("안양로264번길 24")
                .build_no("627-70")
                .latitude(BigDecimal.valueOf(126.939171))
                .longitude(BigDecimal.valueOf(37.3786908))
                .build();

        cafeRepository.save(Cafe.builder()
                .title("모노폴리로그")
                .naver_url("url1")
                .cafeAddress(cafeAddress1)
                .build());
        cafeAddressRepository.save(cafeAddress1);

        CafeAddress cafeAddress2 = CafeAddress.builder()
                .sido("서울")
                .sigungu("성동구")
                .dong("하왕십리동")
                .doro("청계천로 494")
                .build_no("258")
                .latitude(BigDecimal.valueOf(127.031639))
                .longitude(BigDecimal.valueOf(37.5686891))
                .build();

        cafeRepository.save(Cafe.builder()
                .title("로그라운드")
                .naver_url("url2")
                .cafeAddress(cafeAddress2)
                .build());
        cafeAddressRepository.save(cafeAddress2);

        CafeAddress cafeAddress3 = CafeAddress.builder()
                .sido("서울")
                .sigungu("강남구")
                .dong("역삼동")
                .doro("언주로 537")
                .build_no("662-14")
                .latitude(BigDecimal.valueOf(127.040684))
                .longitude(BigDecimal.valueOf(37.5059322))
                .build();

        cafeRepository.save(Cafe.builder()
                .title("로그")
                .naver_url("url3")
                .cafeAddress(cafeAddress3)
                .build());
        cafeAddressRepository.save(cafeAddress3);

        CafeAddress cafeAddress4 = CafeAddress.builder()
                .sido("인천")
                .sigungu("연수구")
                .dong("송도동")
                .doro("송도과학로16번길 13-18")
                .build_no("송도동 174-1")
                .latitude(BigDecimal.valueOf(126.660801))
                .longitude(BigDecimal.valueOf(37.3810369))
                .build();

        cafeRepository.save(Cafe.builder()
                .title("스폰타니티")
                .naver_url("url4")
                .cafeAddress(cafeAddress4)
                .build());
        cafeAddressRepository.save(cafeAddress4);
    }

    @AfterEach
    public void cleanup(){
        cafeRepository.deleteAll();
    }

    @Test
    @DisplayName("새로운 카페 등록하기")
    public void registerNewCafe(){
        // given
        String sido = "경기";
        String sigungu = "안양시 만안구";
        String dong = "안양동";
        String doro = "장내로149번길 53";
        String build_no = "674-81";
        String branch = "안양역점";
        BigDecimal latitude = BigDecimal.valueOf(126.922145);
        BigDecimal longitude = BigDecimal.valueOf(37.4002960);

        CafeAddress cafeAddress = CafeAddress.builder()
                        .sido(sido)
                        .sigungu(sigungu)
                        .dong(dong)
                        .doro(doro)
                        .build_no(build_no)
                        .branch(branch)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();

        String title = "스타벅스";
        String naver_url = "https://map.naver.com/v5/search/스타벅스%20안양일번가/place/37169041?placePath=%3Fentry=pll%26from=nx%26fromNxList=true";

        Cafe cafe = Cafe.builder()
                .title(title)
                .naver_url(naver_url)
                .cafeAddress(cafeAddress)
                .build();

        cafeRepository.save(cafe);
        cafeAddressRepository.save(cafeAddress);

        // when
        Cafe existCafe = cafeRepository.findById(cafe.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카페가 존재하지 않습니다"));
        CafeAddress existCafeAddress = cafeAddressRepository.findById(existCafe.getCafeAddress().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 존재하지 않습니다"));

        // then
        assertThat(existCafe.getTitle()).isEqualTo(title);
        assertThat(existCafe.getNaver_url()).isEqualTo(naver_url);

        CafeAddress joinAddress = existCafe.getCafeAddress();
        assertThat(existCafeAddress.getId()).isEqualTo(joinAddress.getId());
        assertThat(existCafeAddress.getSido()).isEqualTo(joinAddress.getSido());
        assertThat(existCafeAddress.getSigungu()).isEqualTo(joinAddress.getSigungu());
        assertThat(existCafeAddress.getEupmyun()).isEqualTo(joinAddress.getEupmyun());
        assertThat(existCafeAddress.getDoro()).isEqualTo(joinAddress.getDoro());
        assertThat(existCafeAddress.getDong()).isEqualTo(joinAddress.getDong());
        assertThat(existCafeAddress.getBuild_no()).isEqualTo(joinAddress.getBuild_no());
        assertThat(existCafeAddress.getBranch()).isEqualTo(joinAddress.getBranch());
        assertThat(existCafeAddress.getLatitude()).isEqualTo(joinAddress.getLatitude());
        assertThat(existCafeAddress.getLongitude()).isEqualTo(joinAddress.getLongitude());
    }

    @Test
    @DisplayName("키워드를 통해 카페 조회하기")
    public void selectCafeByKeyword(){
        // given
        String keyword = "로그";

        // when
        List<Cafe> cafeList = cafeRepository.findByTitleContains(keyword);

        // then
        assertThat(cafeList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("키워드에 해당하는 카페가 없는 조회하기")
    public void selectCafeFail(){
        // given
        String keyword = "롯데리아";

        // when
        List<Cafe> cafeList = cafeRepository.findByTitleContains(keyword);

        assertThat(cafeList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("카페 목록 조회")
    public void selectCafeList(){
        // when
        List<Cafe> cafeList = cafeRepository.findAll();

        //then
        assertThat(cafeList.size()).isEqualTo(4);
    }
}
