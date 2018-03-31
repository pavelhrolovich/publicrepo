package com.gmail.hrolovic.pavel.result;

import com.gmail.hrolovic.pavel.ProcessingFailedException;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.validation.ValidationError;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

public class PrintStreamResultConsumer implements ResultConsumer {
    private PrintStream printStream;

    public PrintStreamResultConsumer(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void onSuccessfulCalculation(LoanOffer loanOffer) {
        printStream.printf("Requested amount: £%d%n", loanOffer.getLoanAmount());
        printStream.printf("Rate: %s%%%n", getLoanPercentageAsIntegerPercent(loanOffer.getLoanPercentage()));
        printStream.printf("Monthly repayment: £%s%n", loanOffer.getMonthlyRepayment());
        printStream.printf("Total repayment: £%s%n", loanOffer.getTotalRepayment());
    }

    @Override
    public void onFailedCalculation(ProcessingFailedException exception) {
        printStream.println("Application failed to calculate offer due to technical error:");
        printStream.printf("Error type: %s%n", exception.getClass().getSimpleName());
        printStream.printf("Message: %s %n", exception.getMessage());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errorList, OfferCalculationRequest request) {
        printStream.println("Application failed to calculate offer due to input parameters validation errors:");
        errorList.forEach(error -> {
            printStream.printf("Argument: %s, cause: %s%n", error.getArgumentName(), error.getMessage());
        });
    }

    private BigDecimal getLoanPercentageAsIntegerPercent(BigDecimal percentageDecimal) {
        BigDecimal multiply = percentageDecimal.multiply(new BigDecimal("100"));
        return multiply.setScale(1, BigDecimal.ROUND_HALF_UP);
    }


}
