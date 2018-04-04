package com.gmail.hrolovic.pavel;

import com.gmail.hrolovic.pavel.application.FileBasedLoanOfferCalculationModule;
import com.gmail.hrolovic.pavel.request.InputFileBasedOfferCalculationRequest;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.java.Log;

import java.io.File;
import java.io.PrintStream;

public class RateCalculationApplication {


    public static void main(String[] args) {
        RateCalculationApplication rateCalculationApplication = new RateCalculationApplication();
        rateCalculationApplication.runApplication(args);
    }

    private void runApplication(String[] args) {
        if (!validateInputParameters(args, System.out)) {
            return;
        }
        OfferCalculationRequest offerCalculationRequest = InputFileBasedOfferCalculationRequest.parse(args);

        FileBasedLoanOfferCalculationModule fileBasedLoanOfferCalculationModule = new FileBasedLoanOfferCalculationModule();
        Injector injector = Guice.createInjector(fileBasedLoanOfferCalculationModule);
        OfferCalculationRequestProcessor commandLineOfferCalculationRequestProcessor = injector.getInstance(OfferCalculationRequestProcessor.class);
        commandLineOfferCalculationRequestProcessor.processRequest(offerCalculationRequest);
    }

    protected boolean validateInputParameters(String[] args, PrintStream printStream) {
        if (args.length != 3) {
            printStream.println("Invalid usage. Please provide valid filename, month count and loan amount, e.g. filename.csv 36 1500");
            return false;
        }
        String filename = args[0];
        String monthCount = args[1];
        String loanAmount = args[2];

        boolean valid = true;
        File file = new File(filename);
        if (!file.exists()) {
            printStream.printf("File does not exist by path [%s]. Please provide valid filename.%n", file.getAbsolutePath());
            valid = false;
        }

        try {
            Integer.parseInt(monthCount);
        } catch (NumberFormatException e) {
            printStream.printf("Invalid month count: [%s], must be integer.%n", monthCount);
            valid = false;
        }

        try {
            Integer.parseInt(loanAmount);
        } catch (NumberFormatException e) {
            printStream.printf("Invalid loan amount: [%s], must be integer.%n", loanAmount);
            valid = false;
        }
        return valid;
    }

}

