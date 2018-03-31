package com.gmail.hrolovic.pavel.validation;

import com.gmail.hrolovic.pavel.OfferCalculationRequestStub;
import com.gmail.hrolovic.pavel.data.LenderInvestment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceImplTest {
    @InjectMocks
    private ValidationServiceImpl service;


    @Test
    public void shouldValidateMinimumAmount() {
        OfferCalculationRequestStub request = OfferCalculationRequestStub.builder().monthAmount(5).desiredAmount(-100).build();
        List<ValidationError> errors = service.validateInputRequest(new ArrayList<>(), request);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Desired amount", errors.get(0).getArgumentName());
    }

    @Test
    public void shouldValidateMaximumAmount() {
        OfferCalculationRequestStub request = OfferCalculationRequestStub.builder().monthAmount(5).desiredAmount(111100).build();
        List<LenderInvestment> lendersList = new ArrayList<>();
        lendersList.add(LenderInvestment.builder().availableAmount(100000).build());

        List<ValidationError> errors = service.validateInputRequest(lendersList, request);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Desired amount", errors.get(0).getArgumentName());
    }

    @Test
    public void shouldValidate100Increment() {
        OfferCalculationRequestStub request = OfferCalculationRequestStub.builder().monthAmount(5).desiredAmount(1201).build();
        List<ValidationError> errors = service.validateInputRequest(new ArrayList<>(), request);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Desired amount", errors.get(0).getArgumentName());
    }

    @Test
    public void shouldValidateAgainstLendersMaximumAmount() {
        OfferCalculationRequestStub request = OfferCalculationRequestStub.builder().monthAmount(5).desiredAmount(3000).build();
        List<LenderInvestment> lendersList = new ArrayList<>();
        lendersList.add(LenderInvestment.builder().availableAmount(1000).build());
        List<ValidationError> errors = service.validateInputRequest(lendersList, request);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Loan length", errors.get(0).getArgumentName());
    }

    @Test
    public void shouldValidateMaximumLoanLength() {
        OfferCalculationRequestStub request = OfferCalculationRequestStub.builder().monthAmount(90).desiredAmount(1050).build();
        List<LenderInvestment> lendersList = new ArrayList<>();
        lendersList.add(LenderInvestment.builder().availableAmount(1500).build());
        List<ValidationError> errors = service.validateInputRequest(lendersList, request);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Month count", errors.get(0).getArgumentName());
    }

    @Test
    public void shouldValidateMinimumLoanLength() {
        OfferCalculationRequestStub request = OfferCalculationRequestStub.builder().monthAmount(0).desiredAmount(1050).build();
        List<LenderInvestment> lendersList = new ArrayList<>();
        lendersList.add(LenderInvestment.builder().availableAmount(1500).build());
        List<ValidationError> errors = service.validateInputRequest(lendersList, request);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Month count", errors.get(0).getArgumentName());
    }

    @Test
    public void shouldAllowProperLoanLength() {
        OfferCalculationRequestStub request = OfferCalculationRequestStub.builder().monthAmount(5).desiredAmount(1050).build();
        List<LenderInvestment> lendersList = new ArrayList<>();
        lendersList.add(LenderInvestment.builder().availableAmount(1500).build());
        List<ValidationError> errors = service.validateInputRequest(lendersList, request);
        Assert.assertEquals(0, errors.size());
    }
}