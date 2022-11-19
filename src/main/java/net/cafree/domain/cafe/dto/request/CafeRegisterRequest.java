package net.cafree.domain.cafe.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeRegisterRequest {
    @NotNull
    private String title;

    private String sido;

    private String sigungu;

    private String eupmyun;

    private String dong;

    private String doro;

    private String buildNo;

    private String branch;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;
}
