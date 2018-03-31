package com.gmail.hrolovic.pavel.result;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString @Getter @Builder
public class MonthlyPayment {
    private final BigDecimal amountMonthly;
    private final BigDecimal leftAmountPercent;

}
