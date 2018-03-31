package com.gmail.hrolovic.pavel;

import com.gmail.hrolovic.pavel.data.CSVLenderDataProvider;
import com.gmail.hrolovic.pavel.data.LenderDataProvider;
import com.gmail.hrolovic.pavel.processing.LoadCalculationService;
import com.gmail.hrolovic.pavel.processing.LoanCalculationServiceImpl;
import com.gmail.hrolovic.pavel.result.ResultConsumer;
import com.gmail.hrolovic.pavel.validation.ValidationService;
import com.gmail.hrolovic.pavel.validation.ValidationServiceImpl;
import com.google.inject.AbstractModule;

public class IntegrationTestModule  extends AbstractModule {
    private ResultConsumer resultConsumer;

    public IntegrationTestModule(ResultConsumer resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    @Override
    protected void configure() {
        bind(LenderDataProvider.class).to(CSVLenderDataProvider.class);
        bind(ResultConsumer.class).toInstance(resultConsumer);
        bind(LoadCalculationService.class).to(LoanCalculationServiceImpl.class);
        bind(ValidationService.class).to(ValidationServiceImpl.class);
    }
}
