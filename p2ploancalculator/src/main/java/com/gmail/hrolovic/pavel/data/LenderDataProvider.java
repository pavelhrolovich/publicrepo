package com.gmail.hrolovic.pavel.data;

import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.ProcessingFailedException;

import java.util.List;

public interface LenderDataProvider {

    /**
     * Load data from source system.
     *
     * @return list of lenders with amount available and rate.
     *
     */
    List<LenderInvestment> getLendersList(OfferCalculationRequest request) throws ProcessingFailedException;


}
