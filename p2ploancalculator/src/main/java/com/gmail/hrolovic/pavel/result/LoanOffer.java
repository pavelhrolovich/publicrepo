package com.gmail.hrolovic.pavel.result;

import com.gmail.hrolovic.pavel.data.LenderInvestment;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class LoanOffer {
    private final Integer loanAmount;
    private final Integer loanLength;
    private final List<LenderInvestment> involvedLenders;
    private final BigDecimal loanPercentage;
    private final List<MonthlyPayment> monthlyPayments;
    private final BigDecimal monthlyRepayment;
    private final BigDecimal totalRepayment;

    public LoanOffer(Integer loanAmount,
                     Integer loanLength,
                     List<LenderInvestment> involvedLenders,
                     BigDecimal loanPercentage,
                     List<MonthlyPayment> monthlyPayments,
                     BigDecimal monthlyRepayment,
                     BigDecimal totalRepayment) {
        this.loanAmount = loanAmount;
        this.loanLength = loanLength;
        this.involvedLenders = involvedLenders;
        this.loanPercentage = loanPercentage;
        this.monthlyPayments = monthlyPayments;
        this.monthlyRepayment = monthlyRepayment;
        this.totalRepayment = totalRepayment;
    }

}
