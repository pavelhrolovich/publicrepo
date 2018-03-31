package com.gmail.hrolovic.pavel.validation;

import com.gmail.hrolovic.pavel.data.LenderInvestment;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;

import java.util.List;

public interface ValidationService {

    List<ValidationError> validateInputRequest(List<LenderInvestment> lendersList, OfferCalculationRequest request);
}
