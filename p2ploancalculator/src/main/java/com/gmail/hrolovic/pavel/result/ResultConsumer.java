package com.gmail.hrolovic.pavel.result;

import com.gmail.hrolovic.pavel.ProcessingFailedException;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.validation.ValidationError;

import java.util.List;

public interface ResultConsumer {

    void onSuccessfulCalculation(LoanOffer loanOffer);

    void onFailedCalculation(ProcessingFailedException exception);

    void onValidationFailed(List<ValidationError> errorList, OfferCalculationRequest request);
}
