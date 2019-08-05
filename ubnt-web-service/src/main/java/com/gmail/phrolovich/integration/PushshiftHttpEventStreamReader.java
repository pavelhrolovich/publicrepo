package com.gmail.phrolovich.integration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.util.concurrent.CompletableFuture;

/**
 * This is to pay your attention the application scope / singletin is an important here. We only
 * need to read event from stream via single source per application
 */
@Component
@Slf4j
@AllArgsConstructor
public class PushshiftHttpEventStreamReader {
    private static final String URL = "https://stream.pushshift.io/";
    private static final int ONE_SECOND = 1000;

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void postConstruct() {
        CompletableFuture.runAsync(() -> {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(URL);
            try (SseEventSource source = SseEventSource.target(target).build()) {
                source.register(inboundSseEvent -> {
                    RedditStatisticEvent event = new RedditStatisticEvent(this, inboundSseEvent.getName(),
                            inboundSseEvent.readData());
                    applicationEventPublisher.publishEvent(event);
                });
                source.open();
                while(true) {
                    try {
                        Thread.sleep(ONE_SECOND);
                    } catch (InterruptedException e) {
                        log.warn("Waiting thread was interrupted.");
                    }
                }
            }
        });
    }

}


