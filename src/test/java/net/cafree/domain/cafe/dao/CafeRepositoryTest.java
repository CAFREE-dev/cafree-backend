package net.cafree.domain.cafe.dao;

import net.cafree.domain.cafe.domain.Cafe;
import net.cafree.domain.cafe.domain.CafeAddress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CafeRepositoryTest {
    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    CafeAddressRepository cafeAddressRepository;

    @AfterEach
    public void cleanup(){
        cafeRepository.deleteAll();
    }

    @DisplayName("새로운 카페 등록하기")
    @Test
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
        CafeAddress existCafeAddress = cafeAddressRepository.findById(cafeAddress.getId())
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
}
