package com.creditcore.dto.request.recovery;

import com.creditcore.enums.contract.RepaymentCycle;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecoveryProgramCreateRequest {

    @NotNull(message = "상환 횟수는 필수 입니다.")
    @Min(value = 1, message = "상환 횟수는 최소 1이상이어야 합니다.")
    private Integer repaymentCount;

    @NotNull(message = "상환 주기는 필수 입니다.")
    private RepaymentCycle repaymentCycle;
}
