package net.cafree.domain.cafe.dto.request;

import lombok.*;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
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

    public Cafe toCafeEntity(CafeAddress cafeAddress) {
        return Cafe.builder()
                .title(title)
                .likeCount(0)
                .mapUrl("")
                .cafeAddress(cafeAddress)
                .build();
    }

    public CafeAddress toCafeAddressEntity() {
        return CafeAddress.builder()
                .sido(sido)
                .sigungu(sigungu)
                .eupmyun(eupmyun)
                .dong(dong)
                .doro(doro)
                .buildNo(buildNo)
                .branch(branch)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
