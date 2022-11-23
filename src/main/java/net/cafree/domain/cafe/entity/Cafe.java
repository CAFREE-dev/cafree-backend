package net.cafree.domain.cafe.entity;

import lombok.*;
import net.cafree.domain.cafe.dto.response.CafeResponse;
import net.cafree.domain.cafe.dto.response.SimpleCafeResponse;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer like_count;

    @Column(length = 255, nullable = false)
    private String naver_url;

    /* 2022.11.15 - lcomment : 좋아요 수 Default값을 null이 아닌 0으로 지정 */
    @PrePersist
    private void prePersist() {
        if(like_count == null){
            like_count = 0;
        }
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cafe_address_id")
    private CafeAddress cafeAddress;

    @Builder
    public Cafe(String title, int like_count, String naver_url, CafeAddress cafeAddress){
        this.title = title;
        this.like_count = like_count;
        this.naver_url = naver_url;
        this.cafeAddress = cafeAddress;
    }

    public void updateCafe(String title, Integer likeCount, String mapUrl) {
        this.title = title;
        this.like_count = likeCount;
        this.naver_url = mapUrl;
    }

    /* 2022.11.23 - rt3310 : User Entity가 구현되면 거리 계산 값, 북마크 여부 삽입 필요 */
    public CafeResponse toCafeResponse() {
        return CafeResponse.builder()
                .id(id)
                .title(title)
                .mapUrl(naver_url)
                .likeCount(like_count)
                .preview("")
                .isMarked(false)
                .sido(cafeAddress.getSido())
                .sigungu(toCafeResponse().getSigungu())
                .eupmyun(cafeAddress.getEupmyun())
                .dong(cafeAddress.getDong())
                .doro(cafeAddress.getDoro())
                .buildNo(cafeAddress.getBuild_no())
                .branch(cafeAddress.getBranch())
                .latitude(cafeAddress.getLatitude())
                .longitude(cafeAddress.getLongitude())
                .distance(0.0)
                .build();
    }

    /* 2022.11.23 - rt3310 : User Entity가 구현되면 거리 계산 값, 북마크 여부 삽입 필요 */
    public SimpleCafeResponse toSimpleCafeResponse() {
        return SimpleCafeResponse.builder()
                .id(id)
                .title(title)
                .mapUrl(naver_url)
                .likeCount(like_count)
                .preview("")
                .isMarked(false)
                .sido(cafeAddress.getSido())
                .sigungu(toCafeResponse().getSigungu())
                .eupmyun(cafeAddress.getEupmyun())
                .dong(cafeAddress.getDong())
                .doro(cafeAddress.getDoro())
                .buildNo(cafeAddress.getBuild_no())
                .branch(cafeAddress.getBranch())
                .latitude(cafeAddress.getLatitude())
                .longitude(cafeAddress.getLongitude())
                .distance(0.0)
                .build();
    }
}
