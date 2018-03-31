package com.gmail.hrolovic.pavel.data;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVRecord;

@AllArgsConstructor
public class CSVLenderDataRecord {
    private static final String LENDER_COLUMN = "Lender";
    private static final String RATE_COLUMN = "Rate";
    private static final String AVAILABLE_COLUMN = "Available";

    private CSVRecord csvRecord;

    public String getRate() {
        return csvRecord.get(RATE_COLUMN);
    }

    public String getLenderName() {
        return csvRecord.get(LENDER_COLUMN);
    }

    public String getAvailable() {
        return csvRecord.get(AVAILABLE_COLUMN);
    }

    public Long getRowId() {
        return csvRecord.getRecordNumber();
    }

    public boolean isConsistent() {
        if (!csvRecord.isConsistent()) {
            return false;
        }
        String rawRateColumn = getRate();
        try {
            double rate = Double.parseDouble(rawRateColumn.trim());
            if (rate < 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        String rawAmountColumn = getAvailable();
        try {
            int amount = Integer.parseInt(rawAmountColumn.trim());
            if (amount < 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
