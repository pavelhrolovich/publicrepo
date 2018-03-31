package com.gmail.hrolovic.pavel;

import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.result.LoanOffer;
import com.gmail.hrolovic.pavel.result.PrintStreamResultConsumer;
import com.gmail.hrolovic.pavel.result.ResultConsumer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintStream;
import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class IntegrationTest {
    @Mock
    private PrintStream printWriter;

    private OfferCalculationRequestProcessor offerCalculationRequestProcessor;

    @Before
    public void setUp() throws Exception {
        ResultConsumer resultConsumer = new PrintStreamResultConsumer(printWriter);

        IntegrationTestModule testModule = new IntegrationTestModule(resultConsumer);
        Injector injector = Guice.createInjector(testModule);
        offerCalculationRequestProcessor = injector.getInstance(OfferCalculationRequestProcessor.class);
    }

    @Test
    public void shouldCalculate2300RateFor36MonthsLoan() {
        OfferCalculationRequest offerCalculationRequest = OfferCalculationRequestStub.builder()
                .desiredAmount(2300)
                .monthAmount(36)
                .filename("./inputdata.csv")
                .build();
        offerCalculationRequestProcessor.processRequest(offerCalculationRequest);

        Mockito.verify(printWriter).printf("Requested amount: £%d%n", 2300);
        Mockito.verify(printWriter).printf("Rate: %s%%%n", new BigDecimal("7.5"));
        Mockito.verify(printWriter).printf("Monthly repayment: £%s%n", new BigDecimal("71.28"));
        Mockito.verify(printWriter).printf("Total repayment: £%s%n", new BigDecimal("2566.10"));
        Mockito.verifyNoMoreInteractions(printWriter);
    }

    @Test
    public void shouldCalculate2300RateFor2MonthsLoan() {
        OfferCalculationRequest offerCalculationRequest = OfferCalculationRequestStub.builder()
                .desiredAmount(2300)
                .monthAmount(2)
                .filename("./inputdata.csv")
                .build();
        offerCalculationRequestProcessor.processRequest(offerCalculationRequest);

        Mockito.verify(printWriter).printf("Requested amount: £%d%n", 2300);
        Mockito.verify(printWriter).printf("Rate: %s%%%n", new BigDecimal("7.5"));
        Mockito.verify(printWriter).printf("Monthly repayment: £%s%n", new BigDecimal("1160.79"));
        Mockito.verify(printWriter).printf("Total repayment: £%s%n", new BigDecimal("2321.57"));
        Mockito.verifyNoMoreInteractions(printWriter);
    }

    @Test
    public void shouldCalculate1000RateFor36MonthLoan() {
        OfferCalculationRequest offerCalculationRequest = OfferCalculationRequestStub.builder()
                .desiredAmount(1000)
                .monthAmount(36)
                .filename("./inputdata.csv")
                .build();
        offerCalculationRequestProcessor.processRequest(offerCalculationRequest);

        Mockito.verify(printWriter).printf("Requested amount: £%d%n", 1000);
        Mockito.verify(printWriter).printf("Rate: %s%%%n", new BigDecimal("7.0"));
        Mockito.verify(printWriter).printf("Monthly repayment: £%s%n", new BigDecimal("30.78"));
        Mockito.verify(printWriter).printf("Total repayment: £%s%n", new BigDecimal("1108.10"));
        Mockito.verifyNoMoreInteractions(printWriter);
    }

    @Test
    public void shouldCalculate1000RateFor6MonthLoan() {
        OfferCalculationRequest offerCalculationRequest = OfferCalculationRequestStub.builder()
                .desiredAmount(1000)
                .monthAmount(6)
                .filename("./inputdata.csv")
                .build();
        offerCalculationRequestProcessor.processRequest(offerCalculationRequest);

        Mockito.verify(printWriter).printf("Requested amount: £%d%n", 1000);
        Mockito.verify(printWriter).printf("Rate: %s%%%n", new BigDecimal("7.0"));
        Mockito.verify(printWriter).printf("Monthly repayment: £%s%n", new BigDecimal("170.08"));
        Mockito.verify(printWriter).printf("Total repayment: £%s%n", new BigDecimal("1020.45"));
        Mockito.verifyNoMoreInteractions(printWriter);
    }

    @Test
    public void shouldCalculate1100RateFor36MonthLoan() {
        OfferCalculationRequest offerCalculationRequest = OfferCalculationRequestStub.builder()
                .desiredAmount(1100)
                .monthAmount(36)
                .filename("./inputdata.csv")
                .build();
        offerCalculationRequestProcessor.processRequest(offerCalculationRequest);

        Mockito.verify(printWriter).printf("Requested amount: £%d%n", 1100);
        Mockito.verify(printWriter).printf("Rate: %s%%%n", new BigDecimal("7.0"));
        Mockito.verify(printWriter).printf("Monthly repayment: £%s%n", new BigDecimal("33.86"));
        Mockito.verify(printWriter).printf("Total repayment: £%s%n", new BigDecimal("1218.87"));
        Mockito.verifyNoMoreInteractions(printWriter);
    }


}

