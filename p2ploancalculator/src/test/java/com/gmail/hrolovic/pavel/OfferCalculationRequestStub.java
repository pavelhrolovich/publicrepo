package com.gmail.hrolovic.pavel;

import com.gmail.hrolovic.pavel.request.InputFileBasedOfferCalculationRequest;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
public class OfferCalculationRequestStub extends InputFileBasedOfferCalculationRequest{
    private String filename;
    private int desiredAmount;
    private int monthAmount;

}