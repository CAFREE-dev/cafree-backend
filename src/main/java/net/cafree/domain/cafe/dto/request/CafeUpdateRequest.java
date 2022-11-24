package net.cafree.domain.cafe.dto.request;

import lombok.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CafeUpdateRequest(@NotNull String title,
                                @NotNull Integer likeCount,
                                String mapUrl,
                                String sido,
                                String sigungu,
                                String eupmyun,
                                String dong,
                                String doro,
                                String buildNo,
                                String branch,
                                @NotNull BigDecimal latitude,
                                @NotNull BigDecimal longitude) {

    @Builder
    public CafeUpdateRequest{}
}
