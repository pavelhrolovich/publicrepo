package com.gmail.hrolovic.pavel.result;

import com.gmail.hrolovic.pavel.OfferCalculationRequestStub;
import com.gmail.hrolovic.pavel.ProcessingFailedException;
import com.gmail.hrolovic.pavel.validation.ValidationError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrintStreamResultConsumerTest {
    @InjectMocks
    private PrintStreamResultConsumer consumer;
    @Mock
    private PrintStream printStream;

    @Test
    public void shouldPrintLoanOfferData() {
        LoanOffer loanOffer = LoanOffer.builder()
                .loanAmount(100)
                .loanPercentage(new BigDecimal(0.1))
                .loanLength(36)
                .monthlyRepayment(new BigDecimal("20.11"))
                .totalRepayment(new BigDecimal(1001))
                .monthlyPayments(new ArrayList<>())
                .build();
        consumer.onSuccessfulCalculation(loanOffer);

        verify(printStream).printf("Requested amount: £%d%n", 100);
        verify(printStream).printf("Rate: %s%%%n", new BigDecimal("10.0"));
        verify(printStream).printf("Monthly repayment: £%s%n", new BigDecimal("20.11"));
        verify(printStream).printf("Total repayment: £%s%n", new BigDecimal("1001"));
        verifyNoMoreInteractions(printStream);
    }

    @Test
    public void shouldPrintTechnicalErrorData() {
        ProcessingFailedException processingFailedException = Mockito.mock(ProcessingFailedException.class, "processingFailedException");
        when(processingFailedException.getMessage()).thenReturn("Test message");
        consumer.onFailedCalculation(processingFailedException);

        verify(printStream).println("Application failed to calculate offer due to technical error:");
        verify(printStream).printf("Message: %s %n", "Test message");
    }

    @Test
    public void shouldPrintValidationErrors() {
        ProcessingFailedException processingFailedException = Mockito.mock(ProcessingFailedException.class, "processingFailedException");
        when(processingFailedException.getMessage()).thenReturn("Test message");

        ValidationError first = ValidationError.builder().argumentName("arg1").message("message1").build();
        ValidationError second = ValidationError.builder().argumentName("arg2").message("message2").build();

        List<ValidationError> validationErrors = Arrays.asList(first, second);
        consumer.onValidationFailed(validationErrors, OfferCalculationRequestStub.builder().monthAmount(12).monthAmount(100).build());

        verify(printStream).println("Application failed to calculate offer due to input parameters validation errors:");
        verify(printStream).printf("Argument: %s, cause: %s%n", "arg1", "message1");
        verify(printStream).printf("Argument: %s, cause: %s%n", "arg2", "message2");
        verifyNoMoreInteractions(printStream);
    }

}