package com.gmail.hrolovic.pavel.result;

import jakarta.inject.Provider;

public class SystemOutResultConsumerProducer implements Provider<ResultConsumer> {

    @Override
    public ResultConsumer get() {
        return new PrintStreamResultConsumer(System.out);
    }

}
