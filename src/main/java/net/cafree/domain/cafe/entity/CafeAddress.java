package net.cafree.domain.cafe.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cafe_address")
public class CafeAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String sido;

    @Column(length = 100)
    private String sigungu;

    @Column(length = 100)
    private String eupmyun;

    @Column(length = 20)
    private String dong;

    @Column(length = 80)
    private String doro;

    @Column(length = 10)
    private String build_no;

    @Column(length = 100)
    private String branch;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Builder
    public CafeAddress(
            String sido,
            String sigungu,
            String eupmyun,
            String dong,
            String doro,
            String build_no,
            String branch,
            BigDecimal latitude,
            BigDecimal longitude
    ){
        this.sido = sido;
        this.sigungu = sigungu;
        this.eupmyun = eupmyun;
        this.dong = dong;
        this.doro = doro;
        this.build_no = build_no;
        this.branch = branch;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
