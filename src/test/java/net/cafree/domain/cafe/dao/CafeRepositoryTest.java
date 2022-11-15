package net.cafree.domain.cafe.dao;

import net.cafree.domain.cafe.domain.Cafe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CafeRepositoryTest {
    @Autowired
    CafeRepository cafeRepository;

    @AfterEach
    public void cleanup(){
        cafeRepository.deleteAll();
    }

    @DisplayName("새로운 카페 등록하기")
    @Test
    public void registerNewCafe(){
        // given
        String title = "title_example";
        String naver_url = "naver_url_example";

        cafeRepository.save(Cafe.builder()
                .title(title)
                .naver_url(naver_url)
                .build());

        // when
        List<Cafe> cafeList = cafeRepository.findAll();

        // then
        Cafe cafe = cafeList.get(0);
        assertThat(cafe.getTitle()).isEqualTo(title);
        assertThat(cafe.getNaver_url()).isEqualTo(naver_url);
    }
}
