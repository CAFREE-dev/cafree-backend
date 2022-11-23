package net.cafree.domain.cafe.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleCafeResponse {
    private Long id;

    private String title;

    private String mapUrl;

    private Integer likeCount;

    private String preview;

    private Boolean isMarked;

    private String sido;

    private String sigungu;

    private String eupmyun;

    private String dong;

    private String doro;

    private String buildNo;

    private String branch;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Double distance;

    @Builder
    public SimpleCafeResponse(Long id,
                              String title,
                              String mapUrl,
                              Integer likeCount,
                              String preview,
                              Boolean isMarked,
                              String sido,
                              String sigungu,
                              String eupmyun,
                              String dong,
                              String doro,
                              String buildNo,
                              String branch,
                              BigDecimal latitude,
                              BigDecimal longitude,
                              Double distance) {
        this.id = id;
        this.title = title;
        this.mapUrl = mapUrl;
        this.likeCount = likeCount;
        this.preview = preview;
        this.isMarked = isMarked;
        this.sido = sido;
        this.sigungu = sigungu;
        this.eupmyun = eupmyun;
        this.dong = dong;
        this.doro = doro;
        this.buildNo = buildNo;
        this.branch = branch;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }
}