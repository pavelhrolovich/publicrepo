package com.gmail.hrolovic.pavel;

import com.gmail.hrolovic.pavel.data.LenderDataProvider;
import com.gmail.hrolovic.pavel.processing.LoadCalculationService;
import com.gmail.hrolovic.pavel.result.LoanOffer;
import com.gmail.hrolovic.pavel.result.ResultConsumer;
import com.gmail.hrolovic.pavel.validation.ValidationError;
import com.gmail.hrolovic.pavel.validation.ValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferCalculationRequestProcessorTest {
    @InjectMocks
    private OfferCalculationRequestProcessor processor;

    @Mock
    private LenderDataProvider dataProvider;
    @Mock
    private LoadCalculationService loadCalculationService;
    @Mock
    private ResultConsumer resultConsumer;
    @Mock
    private ValidationService validationService;


    @Test
    public void shouldReportErrorToResultConsumer() throws ProcessingFailedException {
        ProcessingFailedException error = new ProcessingFailedException("test message");
        when(dataProvider.getLendersList(any())).thenThrow(error);

        processor.processRequest(new OfferCalculationRequestStub("aaa", 100, 10));
        verify(resultConsumer).onFailedCalculation(error);
    }

    @Test
    public void shouldPassOfferToResultConsumer() throws ProcessingFailedException {
        when(dataProvider.getLendersList(any())).thenReturn(new ArrayList<>());
        LoanOffer loanOffer = LoanOffer.builder().loanLength(100).build();
        when(loadCalculationService.calculateOffer(any(), any())).thenReturn(loanOffer);
        when(validationService.validateInputRequest(any(), any())).thenReturn(new ArrayList<>());
        processor.processRequest(new OfferCalculationRequestStub("aaa", 100, 10));

        verify(resultConsumer).onSuccessfulCalculation(loanOffer);
    }
    @Test
    public void shouldReportValidationErrorsToResultConsumer() throws ProcessingFailedException {
        when(dataProvider.getLendersList(any())).thenReturn(new ArrayList<>());
        when(validationService.validateInputRequest(any(), any())).thenReturn(Arrays.asList(ValidationError.builder().build()));
        processor.processRequest(new OfferCalculationRequestStub("aaa", 100, 10));

        verify(resultConsumer).onValidationFailed(anyList(), any());
    }

}