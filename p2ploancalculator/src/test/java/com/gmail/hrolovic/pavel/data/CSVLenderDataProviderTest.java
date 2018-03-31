package com.gmail.hrolovic.pavel.data;

import com.gmail.hrolovic.pavel.OfferCalculationRequestStub;
import com.gmail.hrolovic.pavel.ProcessingFailedException;
import com.gmail.hrolovic.pavel.request.OfferCalculationRequest;
import org.apache.commons.csv.CSVParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class CSVLenderDataProviderTest {
    private CSVLenderDataProvider loader;

    @Before
    public void setUp() throws Exception {
        loader = new CSVLenderDataProvider();
    }

    @Test(expected = ProcessingFailedException.class)
    public void shouldReportErrorIfFileDoesNotExists() throws Exception {
        OfferCalculationRequest offerCalculationRequest = OfferCalculationRequestStub.builder().filename("fasdfasdf.csv").monthAmount(1000).build();
        loader.getLendersList(offerCalculationRequest);
    }

    @Test
    public void shouldNotReportErrorIfFileDoesExists() throws Exception {
        OfferCalculationRequest offerCalculationRequest = OfferCalculationRequestStub.builder()
                .filename("./src/test/resources/10BorrowersData.csv")
                .monthAmount(1000)
                .build();
        loader.getLendersList(offerCalculationRequest);
    }

    @Test
    public void shouldLoadEmptyFile() throws IOException {
        File testDataFile = new File("./src/test/resources/EmptyData.csv");
        List<LenderInvestment> lenderInvestments = loader.parseFile(testDataFile);
        Assert.assertNotNull(lenderInvestments);
        Assert.assertEquals(0, lenderInvestments.size());
    }

    @Test
    public void shouldLoadSevenRows() throws IOException {
        File testDataFile = new File("./src/test/resources/10BorrowersData.csv");
        List<LenderInvestment> lenderInvestments = loader.parseFile(testDataFile);
        Assert.assertNotNull(lenderInvestments);
        Assert.assertEquals(7, lenderInvestments.size());

        LenderInvestment lenderInvestment = lenderInvestments.get(6);
        Assert.assertEquals("Maria", lenderInvestment.getLender());
        Assert.assertEquals(0.071, lenderInvestment.getRate(), 0);
        Assert.assertEquals(60, lenderInvestment.getAvailableAmount());
        Assert.assertEquals(new Long(7), lenderInvestment.getId());
    }

    @Test
    public void shouldIgnoreEmptyLines() throws IOException {
        File testDataFile = new File("./src/test/resources/EmptyRowData.csv");
        List<LenderInvestment> lenderInvestments = loader.parseFile(testDataFile);
        Assert.assertNotNull(lenderInvestments);
        Assert.assertEquals(1, lenderInvestments.size());
    }

    @Test
    public void shouldProcessRecordAsConsistent() throws IOException {
        CSVParser csvRecords = CSVLenderDataProvider.FILE_FORMAT.parse(new FileReader("./src/test/resources/ConsistentRowTestData.csv"));
        List<CSVLenderDataRecord> allRecords = csvRecords.getRecords().stream()
                .map(CSVLenderDataRecord::new)
                .collect(Collectors.toList());
        Assert.assertFalse(allRecords.get(0).isConsistent());
        Assert.assertTrue(allRecords.get(1).isConsistent());
        Assert.assertTrue(allRecords.get(2).isConsistent());
        Assert.assertFalse(allRecords.get(3).isConsistent());
        Assert.assertFalse(allRecords.get(4).isConsistent());
        Assert.assertTrue(allRecords.get(5).isConsistent());
        Assert.assertTrue(allRecords.get(6).isConsistent());
    }


}