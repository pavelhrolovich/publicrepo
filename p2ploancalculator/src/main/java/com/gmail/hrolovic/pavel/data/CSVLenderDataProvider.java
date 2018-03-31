package com.gmail.hrolovic.pavel.data;

import com.gmail.hrolovic.pavel.request.InputFileBasedOfferCalculationRequest;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import com.gmail.hrolovic.pavel.ProcessingFailedException;
import lombok.extern.java.Log;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CSVLenderDataProvider implements LenderDataProvider {
    static final CSVFormat FILE_FORMAT;

    static {
        FILE_FORMAT = CSVFormat.DEFAULT
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames()
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase();
    }

    protected List<LenderInvestment> parseFile(File dataFile) throws IOException {
        Reader inputReader = new FileReader(dataFile);

        Iterable<CSVRecord> records = FILE_FORMAT.parse(inputReader);

        Stream<CSVRecord> recordsStream = StreamSupport.stream(records.spliterator(), false);

        return recordsStream
                .map(CSVLenderDataRecord::new)
                .filter(CSVLenderDataRecord::isConsistent)
                .map(record -> {
                    String lender = record.getLenderName();
                    String rawRateString = record.getRate();
                    double rate = Double.parseDouble(rawRateString);
                    Integer available = Integer.parseInt(record.getAvailable());
                    return new LenderInvestment(record.getRowId(), lender, rate, available);
                })
                .collect(Collectors.toCollection(ArrayList::new));
        // Array list is trade off between sorting speed and data loading speed.
        // The application assumes list of lenders won't be large, so array resize + data copy will not happen often.
    }


    @Override
    public List<LenderInvestment> getLendersList(OfferCalculationRequest offerCalculationRequest) throws ProcessingFailedException {
        if (!InputFileBasedOfferCalculationRequest.class.isAssignableFrom(offerCalculationRequest.getClass())) {
            throw new ProcessingFailedException("Configuration error. Only offer of type InputFileBasedOfferCalculationRequest is supported");
        }
        InputFileBasedOfferCalculationRequest inputFileBasedOfferCalculationRequest = InputFileBasedOfferCalculationRequest.class.cast(offerCalculationRequest);
        String filename = inputFileBasedOfferCalculationRequest.getFilename();
        File file = new File(filename);
        try {
            return parseFile(file);
        } catch (IOException e) {
            throw new ProcessingFailedException("Unable to load data file.", e);
        }
    }
}
