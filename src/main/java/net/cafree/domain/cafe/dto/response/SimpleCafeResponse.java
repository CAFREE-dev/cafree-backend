package net.cafree.domain.cafe.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

public record SimpleCafeResponse(Long id,
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