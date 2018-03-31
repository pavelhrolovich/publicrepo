package com.gmail.hrolovic.pavel.request;

import org.junit.Test;

import static org.junit.Assert.*;

public class InputFileBasedOfferCalculationRequestTest {

    @Test
    public void shouldParseValidInputData() {
        InputFileBasedOfferCalculationRequest request = InputFileBasedOfferCalculationRequest.parse(new String[]{"aa", "2", "145"});
        assertEquals("aa", request.getFilename());
        assertEquals(145, request.getDesiredAmount());
        assertEquals(2, request.getMonthAmount());
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowErrorForInvalidNumber() {
        InputFileBasedOfferCalculationRequest request = InputFileBasedOfferCalculationRequest.parse(new String[]{"aa", "34", "145A"});
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowErrorForInvalidNumberForLoanLength() {
        InputFileBasedOfferCalculationRequest request = InputFileBasedOfferCalculationRequest.parse(new String[]{"aa", "12A", "145"});
    }
}