package net.cafree.domain.cafe.dto.request;

import lombok.*;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CafeRegisterRequest(@NotNull String title,
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
    public CafeRegisterRequest{}

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
