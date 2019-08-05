package com.gmail.phrolovich.api;

public enum TimePeriod {
    ONE_MINUTE(1),
    FIVE_MINUTE(5),
    ONE_HOUR(60),
    ONE_DAY(60 * 24),
    ALL(-1);

    private int sliceCount;

    TimePeriod(int sliceCount) {
        this.sliceCount = sliceCount;
    }

    public int getSliceCount() {
        return sliceCount;
    }
}
