package com.gmail.hrolovic.pavel.data;

import lombok.*;

@Getter @ToString @EqualsAndHashCode()
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class LenderInvestment {
    private Long id;
    private String lender;
    private double rate;
    private int availableAmount;
}
