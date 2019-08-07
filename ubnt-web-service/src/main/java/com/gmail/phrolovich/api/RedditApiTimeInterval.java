package com.gmail.phrolovich.api;

public enum RedditApiTimeInterval {
    ONE_MINUTE(1),
    FIVE_MINUTE(5),
    ONE_HOUR(60),
    ONE_DAY(60 * 24),
    ALL(-1);

    private int slicesPerPeriod;

    RedditApiTimeInterval(int slicesPerPeriod) {
        this.slicesPerPeriod = slicesPerPeriod;
    }

    public int getSlicesPerPeriod() {
        return slicesPerPeriod;
    }
}
