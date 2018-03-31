package com.gmail.hrolovic.pavel.processing;

import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.data.LenderInvestment;
import com.gmail.hrolovic.pavel.result.LoanOffer;

import java.util.List;

public interface LoadCalculationService {
    /**
     * Calculates as low a rate to the borrower as is possible to ensure that quotes are as competitive as they
     * can be against our competitors'.
     * 
     * @param request loan calculation request
     * @param investments list of borrowers
     * @return The best loan offer
     */
    LoanOffer calculateOffer(OfferCalculationRequest request, List<LenderInvestment> investments);
}
