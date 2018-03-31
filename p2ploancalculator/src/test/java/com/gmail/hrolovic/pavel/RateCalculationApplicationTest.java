package com.gmail.hrolovic.pavel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.PrintStream;

public class RateCalculationApplicationTest {
    private RateCalculationApplication rateCalculationApplication;
    private PrintStream printStream;

    @Before
    public void setUp() throws Exception {
        rateCalculationApplication = new RateCalculationApplication();
        printStream = Mockito.mock(PrintStream.class);
    }

    @Test
    public void shouldNotReportAnyError() {
        rateCalculationApplication.validateInputParameters(new String[]{"./src/test/resources/EmptyData.csv", "36", "1500"}, printStream);
        Mockito.verifyNoMoreInteractions(printStream);
    }

    @Test
    public void shouldReportMissingFileError() {
        rateCalculationApplication.validateInputParameters(new String[]{"./src/test/resources/EmptyData1.csv", "36", "1500"}, printStream);
        Mockito.verify(printStream).printf("File does not exist by path [%s]. Please provide valid filename.%n", new File("./src/test/resources/EmptyData1.csv").getAbsolutePath());
        Mockito.verifyNoMoreInteractions(printStream);
    }

    @Test
    public void shouldReportInvalidNumberForAmount() {
        rateCalculationApplication.validateInputParameters(new String[]{"./src/test/resources/EmptyData.csv", "36", "1500A"}, printStream);
        Mockito.verify(printStream).printf("Invalid loan amount: [%s], must be integer.%n", "1500A");
        Mockito.verifyNoMoreInteractions(printStream);
    }

    @Test
    public void shouldReportInvalidNumberForLoanLength() {
        rateCalculationApplication.validateInputParameters(new String[]{"./src/test/resources/EmptyData.csv", "36A", "1500"}, printStream);
        Mockito.verify(printStream).printf("Invalid month count: [%s], must be integer.%n", "36A");
        Mockito.verifyNoMoreInteractions(printStream);
    }

    @Test
    public void shouldReportEmptyArgs() {
        rateCalculationApplication.validateInputParameters(new String[0], System.out);
    }
}