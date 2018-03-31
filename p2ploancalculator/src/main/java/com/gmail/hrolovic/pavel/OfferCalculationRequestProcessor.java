package com.gmail.hrolovic.pavel;

import com.gmail.hrolovic.pavel.data.LenderDataProvider;
import com.gmail.hrolovic.pavel.data.LenderInvestment;
import com.gmail.hrolovic.pavel.processing.LoadCalculationService;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.result.LoanOffer;
import com.gmail.hrolovic.pavel.result.ResultConsumer;
import com.gmail.hrolovic.pavel.validation.ValidationError;
import com.gmail.hrolovic.pavel.validation.ValidationService;

import javax.inject.Inject;
import java.util.List;

public class OfferCalculationRequestProcessor {
    @Inject
    private LenderDataProvider dataProvider;
    @Inject
    private LoadCalculationService loadCalculationService;
    @Inject
    private ResultConsumer resultConsumer;
    @Inject
    private ValidationService validationService;

    public void processRequest(OfferCalculationRequest request) {
        try {
            List<LenderInvestment> lendersList = dataProvider.getLendersList(request);
            List<ValidationError> errorList = validationService.validateInputRequest(lendersList, request);
            if (!errorList.isEmpty()) {
                resultConsumer.onValidationFailed(errorList, request);
            } else {
                LoanOffer loanOffer = loadCalculationService.calculateOffer(request, lendersList);
                resultConsumer.onSuccessfulCalculation(loanOffer);
            }
        } catch (ProcessingFailedException failedCalculation) {
            resultConsumer.onFailedCalculation(failedCalculation);
        }
    }
}
