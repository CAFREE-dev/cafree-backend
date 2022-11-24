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
    public SimpleCafeResponse{}
}