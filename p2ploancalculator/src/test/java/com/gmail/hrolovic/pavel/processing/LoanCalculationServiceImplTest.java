package com.gmail.hrolovic.pavel.processing;

import com.gmail.hrolovic.pavel.OfferCalculationRequestStub;
import com.gmail.hrolovic.pavel.data.LenderInvestment;
import com.gmail.hrolovic.pavel.result.LoanOffer;
import com.gmail.hrolovic.pavel.result.MonthlyPayment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LoanCalculationServiceImplTest {
    @InjectMocks
    private LoanCalculationServiceImpl service;

    private List<LenderInvestment> lenderInvestments;

    private List<LenderInvestment> smallLenderInvestments;

    @Before
    public void setUp() throws Exception {
        lenderInvestments = new ArrayList<>(10);
        lenderInvestments.add(LenderInvestment.builder().lender("Bob").rate(0.075).availableAmount(640).build());//5
        lenderInvestments.add(LenderInvestment.builder().lender("Jane").rate(0.069).availableAmount(480).build()); //1
        lenderInvestments.add(LenderInvestment.builder().lender("Fred").rate(0.071).availableAmount(520).build()); // 2
        lenderInvestments.add(LenderInvestment.builder().lender("Mary").rate(0.104).availableAmount(170).build());//7
        lenderInvestments.add(LenderInvestment.builder().lender("John").rate(0.081).availableAmount(320).build());//6
        lenderInvestments.add(LenderInvestment.builder().lender("Dave").rate(0.074).availableAmount(140).build());//4
        lenderInvestments.add(LenderInvestment.builder().lender("Angela").rate(0.071).availableAmount(60).build()); //3

        LenderInvestment borrower1 = LenderInvestment.builder().rate(0.01).availableAmount(10).id(1L).build();
        LenderInvestment borrower2 = LenderInvestment.builder().rate(0.02).availableAmount(15).id(2L).build();
        LenderInvestment borrower3 = LenderInvestment.builder().rate(0.03).availableAmount(25).id(3L).build();
        LenderInvestment borrower4 = LenderInvestment.builder().rate(0.04).availableAmount(40).id(4L).build();
        LenderInvestment borrower5 = LenderInvestment.builder().rate(0.05).availableAmount(100).id(5L).build();

        smallLenderInvestments = Arrays.asList(borrower1, borrower2, borrower3, borrower4, borrower5);
    }

    @Test
    public void shouldCalculateOfferFor1000Loan() throws Exception {
        OfferCalculationRequestStub requestStub = OfferCalculationRequestStub.builder()
                .monthAmount(36)
                .desiredAmount(1000)
                .build();
        LoanOffer loanOffer = service.calculateOffer(requestStub, lenderInvestments);

        assertEquals(loanOffer.getLoanAmount(), new Integer(1000));
        assertEquals(loanOffer.getLoanLength(), new Integer(36));
        assertEquals(loanOffer.getMonthlyPayments().size(), 36);
        assertEquals(loanOffer.getInvolvedLenders().size(), 2);
        assertEquals(loanOffer.getInvolvedLenders().get(0).getLender(), "Jane");
        assertEquals(loanOffer.getInvolvedLenders().get(1).getLender(), "Fred");

        assertEquals(new BigDecimal("0.070"), loanOffer.getLoanPercentage());
        assertEquals(new BigDecimal("30.78"), loanOffer.getMonthlyRepayment());
        assertEquals(new BigDecimal("1108.10"), loanOffer.getTotalRepayment());
    }

    @Test
    public void shouldCalculateOfferFor2300Loan() throws Exception {
        OfferCalculationRequestStub requestStub = OfferCalculationRequestStub.builder()
                .monthAmount(36)
                .desiredAmount(2300)
                .build();
        LoanOffer loanOffer = service.calculateOffer(requestStub, lenderInvestments);

        assertEquals(loanOffer.getLoanAmount(), new Integer(2300));
        assertEquals(loanOffer.getLoanLength(), new Integer(36));
        assertEquals(loanOffer.getMonthlyPayments().size(), 36);
        assertEquals(loanOffer.getInvolvedLenders().size(), 7);
        assertEquals(loanOffer.getInvolvedLenders().get(0).getLender(), "Jane");
        assertEquals(loanOffer.getInvolvedLenders().get(1).getLender(), "Fred");
        assertEquals(loanOffer.getInvolvedLenders().get(2).getLender(), "Angela");
        assertEquals(loanOffer.getInvolvedLenders().get(3).getLender(), "Dave");
        assertEquals(loanOffer.getInvolvedLenders().get(4).getLender(), "Bob");
        assertEquals(loanOffer.getInvolvedLenders().get(5).getLender(), "John");
        assertEquals(loanOffer.getInvolvedLenders().get(6).getLender(), "Mary");

        assertEquals(new BigDecimal("0.075"), loanOffer.getLoanPercentage());
        assertEquals(new BigDecimal("71.28"), loanOffer.getMonthlyRepayment());
        assertEquals(new BigDecimal("2566.10"), loanOffer.getTotalRepayment());
    }

    @Test
    public void shouldCalculatePaymentFor1100Loan() throws Exception {
        OfferCalculationRequestStub requestStub = OfferCalculationRequestStub.builder()
                .monthAmount(36)
                .desiredAmount(1100)
                .build();
        LoanOffer loanOffer = service.calculateOffer(requestStub, lenderInvestments);

        assertEquals(loanOffer.getLoanAmount(), new Integer(1100));
        assertEquals(loanOffer.getLoanLength(), new Integer(36));
        assertEquals(loanOffer.getMonthlyPayments().size(), 36);
        assertEquals(loanOffer.getInvolvedLenders().size(), 4);
        assertEquals(loanOffer.getInvolvedLenders().get(0).getLender(), "Jane");
        assertEquals(loanOffer.getInvolvedLenders().get(1).getLender(), "Fred");
        assertEquals(loanOffer.getInvolvedLenders().get(2).getLender(), "Angela");
        assertEquals(loanOffer.getInvolvedLenders().get(3).getLender(), "Dave");

        assertEquals(new BigDecimal("0.070"), loanOffer.getLoanPercentage());
        assertEquals(new BigDecimal("33.86"), loanOffer.getMonthlyRepayment());
        assertEquals(new BigDecimal("1218.87"), loanOffer.getTotalRepayment());
    }

    @Test
    public void shouldReturn36MonthPayment() {
        List<MonthlyPayment> monthlyPayments = service.getMonthlyPayments(new BigDecimal(1000), 36, new BigDecimal(0.070), new BigDecimal(27.78));
        assertEquals(36, monthlyPayments.size());
        MonthlyPayment monthlyPayment = monthlyPayments.get(0);
        assertEquals(new BigDecimal("27.78"), monthlyPayment.getAmountMonthly().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("5.84"), monthlyPayment.getLeftAmountPercent().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void shouldCalculateMonthProportion() {
        assertEquals(new BigDecimal("70.00"), service.getProportion(LenderInvestment.builder().rate(0.07).availableAmount(1000).build()));
        assertEquals(new BigDecimal("33.12"), service.getProportion(LenderInvestment.builder().rate(0.069).availableAmount(480).build()));
        assertEquals(new BigDecimal("36.40"), service.getProportion(LenderInvestment.builder().rate(0.070).availableAmount(520).build()));
    }

    @Test
    public void shouldReturnEmptyListIfNoBorrowers() {
        List<LenderInvestment> lenderInvestments = service.fetchLendersForAmount(1000, new ArrayList<>());
        Assert.assertEquals(0, lenderInvestments.size());
    }

    @Test
    public void shouldReturnEmptyListIfNotEnoughLendersLoans() {
        List<LenderInvestment> lenderInvestments = service.fetchLendersForAmount(1000, smallLenderInvestments);
        Assert.assertEquals(0, lenderInvestments.size());
    }

    @Test
    public void shouldReturnListWithBorrowers() {
        List<LenderInvestment> lenderInvestments = service.fetchLendersForAmount(10, smallLenderInvestments);
        Assert.assertEquals(1, lenderInvestments.size());

        lenderInvestments = service.fetchLendersForAmount(5, smallLenderInvestments);
        Assert.assertEquals(1, lenderInvestments.size());

        lenderInvestments = service.fetchLendersForAmount(25, smallLenderInvestments);
        Assert.assertEquals(2, lenderInvestments.size());

        lenderInvestments = service.fetchLendersForAmount(15, smallLenderInvestments);
        Assert.assertEquals(2, lenderInvestments.size());


        lenderInvestments = service.fetchLendersForAmount(50, smallLenderInvestments);
        Assert.assertEquals(3, lenderInvestments.size());

        lenderInvestments = service.fetchLendersForAmount(90, smallLenderInvestments);
        Assert.assertEquals(4, lenderInvestments.size());

        lenderInvestments = service.fetchLendersForAmount(190, smallLenderInvestments);
        Assert.assertEquals(5, lenderInvestments.size());

    }
}