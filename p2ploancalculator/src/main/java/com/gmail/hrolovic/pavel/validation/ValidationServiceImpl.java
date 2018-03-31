package com.gmail.hrolovic.pavel.validation;

import com.gmail.hrolovic.pavel.data.LenderInvestment;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;

import java.util.ArrayList;
import java.util.List;

public class ValidationServiceImpl implements ValidationService {
    private static final int MIN_AMOUNT = 1000;
    private static final int MAX_AMOUNT = 20000;
    private static final int AMOUNT_STEP = 50;

    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 84;

    @Override
    public List<ValidationError> validateInputRequest(List<LenderInvestment> lendersList, OfferCalculationRequest request) {
        List<ValidationError> errors = new ArrayList<>();
        int desiredAmount = request.getDesiredAmount();
        if (desiredAmount < MIN_AMOUNT || desiredAmount > MAX_AMOUNT || desiredAmount % AMOUNT_STEP != 0) {
            // 2. If requested amount is in range
            // 3. If amount is incremented by 50GBP
            errors.add(ValidationError.builder().argumentName("Desired amount").message("Desired amount is any 50 between 1000 and 20000").build());
            return errors;
        }
        int maximumAvailableAmount = lendersList.stream().mapToInt(LenderInvestment::getAvailableAmount).sum();
        if (maximumAvailableAmount < desiredAmount) {
            // 1. If borrowers have enough sum
            errors.add(ValidationError.builder()
                    .argumentName("Loan length")
                    .message(String.format("It is not possible to provide a quote [%d] at that time because lenders cannot afford it.", desiredAmount))
                    .build()
            );
        }
        int monthAmount = request.getMonthAmount();
        if (monthAmount < MIN_MONTH || monthAmount > MAX_MONTH) {
            // 4. If loan length is between expected 1 and 84.
            errors.add(ValidationError.builder().argumentName("Month count").message("Month count is between 1 and 84 months").build());
            return errors;
        }
        return errors;
    }
}
