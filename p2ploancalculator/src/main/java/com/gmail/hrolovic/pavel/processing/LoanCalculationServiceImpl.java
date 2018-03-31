package com.gmail.hrolovic.pavel.processing;

import com.gmail.hrolovic.pavel.data.LenderInvestment;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.result.LoanOffer;
import com.gmail.hrolovic.pavel.result.MonthlyPayment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LoanCalculationServiceImpl implements LoadCalculationService {

    @Override
    public LoanOffer calculateOffer(OfferCalculationRequest offerCalculationRequest, List<LenderInvestment> investments) {
        int desiredAmount = offerCalculationRequest.getDesiredAmount();
        List<LenderInvestment> lenderInvestments = fetchLendersForAmount(desiredAmount, investments);

        return buildOffer(desiredAmount, offerCalculationRequest.getMonthAmount(), lenderInvestments);
    }

    private LoanOffer buildOffer(int loanAmount, int loanLength, List<LenderInvestment> involvedLenders) {
        BigDecimal totalProportion = involvedLenders.stream().map(this::getProportion).reduce(BigDecimal::add).get();
        BigDecimal loanAmountDecimal = new BigDecimal(loanAmount);
        BigDecimal loanPercentage = totalProportion.divide(loanAmountDecimal, 3, BigDecimal.ROUND_HALF_UP);

        BigDecimal amountMonthly = loanAmountDecimal.divide(new BigDecimal(loanLength), 2, RoundingMode.CEILING);
        List<MonthlyPayment> monthlyPayments = getMonthlyPayments(loanAmountDecimal, loanLength, loanPercentage, amountMonthly);
        BigDecimal totalLoanPercents = monthlyPayments.stream().map(MonthlyPayment::getLeftAmountPercent).reduce(BigDecimal::add).get();
        BigDecimal averagePercentMonthly = totalLoanPercents.divide(new BigDecimal(loanLength), 2, RoundingMode.HALF_UP);

        BigDecimal monthlyRepayment = amountMonthly.add(averagePercentMonthly);
        BigDecimal totalRepayment = loanAmountDecimal.add(totalLoanPercents);

        return LoanOffer.builder()
                .loanAmount(loanAmount)
                .loanLength(loanLength)
                .involvedLenders(involvedLenders)
                .loanPercentage(loanPercentage)
                .monthlyPayments(monthlyPayments)
                .monthlyRepayment(monthlyRepayment)
                .totalRepayment(totalRepayment)
                .build();
    }

    protected List<MonthlyPayment> getMonthlyPayments(BigDecimal loanAmount, int loanLength,
                                                      BigDecimal loanPercentage, BigDecimal amountMonthly) {
        BigDecimal percentMonthly = loanPercentage.divide(new BigDecimal(12), 10, RoundingMode.HALF_UP);
        List<MonthlyPayment> monthlyPayments = new ArrayList<>();
        for (int i = 0; i < loanLength; i++) {
            BigDecimal leftAmountPercent = loanAmount.multiply(percentMonthly).setScale(2, RoundingMode.CEILING);

            MonthlyPayment payment = MonthlyPayment.builder()
                    .amountMonthly((i < loanLength - 1) ? amountMonthly : loanAmount)
                    .leftAmountPercent(leftAmountPercent)
                    .build();
            loanAmount = loanAmount.subtract(amountMonthly);
            monthlyPayments.add(payment);
        }
        return monthlyPayments;
    }

    protected BigDecimal getProportion(LenderInvestment lender) {
        BigDecimal rate = new BigDecimal(lender.getRate());
        int availableAmount = lender.getAvailableAmount();
        return rate.multiply(new BigDecimal(availableAmount)).setScale(2, RoundingMode.HALF_UP);
    }

    protected List<LenderInvestment> fetchLendersForAmount(Integer loanAmount, List<LenderInvestment> lendersList) {
        lendersList.sort(
                Comparator.comparing(LenderInvestment::getRate)
        );
        List<LenderInvestment> involvedLenders = new ArrayList<>();
        for (LenderInvestment lender : lendersList) {
            Integer availableAmount = lender.getAvailableAmount();
            if (availableAmount > loanAmount) {
                LenderInvestment newLender = LenderInvestment.builder() // Might be better to use another entity as a result
                        .lender(lender.getLender())
                        .rate(lender.getRate())
                        .availableAmount(loanAmount)
                        .build();
                involvedLenders.add(newLender);
                loanAmount = 0;
            } else {
                involvedLenders.add(lender);
                loanAmount -= availableAmount;
            }
            if (loanAmount == 0) {
                return involvedLenders;
            }
        }
        return new ArrayList<>();
    }


}
