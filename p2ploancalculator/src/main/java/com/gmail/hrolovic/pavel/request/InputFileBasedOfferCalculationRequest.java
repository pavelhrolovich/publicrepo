package com.gmail.hrolovic.pavel.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class InputFileBasedOfferCalculationRequest implements OfferCalculationRequest {
    private static final int MONTH_AMOUNT = 36;

    private String filename;
    private int desiredAmount;
    private int monthAmount;

    protected InputFileBasedOfferCalculationRequest() {

    }

    public static InputFileBasedOfferCalculationRequest parse(String... args) {
        InputFileBasedOfferCalculationRequest offerCalculationRequest = new InputFileBasedOfferCalculationRequest();
        offerCalculationRequest.setFilename(args[0]);
        offerCalculationRequest.setMonthAmount(Integer.parseInt(args[1]));
        offerCalculationRequest.setDesiredAmount(Integer.parseInt(args[2]));
        return offerCalculationRequest;
    }

}
