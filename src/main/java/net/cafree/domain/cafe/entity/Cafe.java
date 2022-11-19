package net.cafree.domain.cafe.entity;

import lombok.*;

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
}
