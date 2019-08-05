package com.gmail.phrolovich.integration;

import com.gmail.phrolovich.storage.SubredditStreamData;
import com.gmail.phrolovich.storage.RedditStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@AllArgsConstructor
public class RedditEventConsumer implements ApplicationListener<RedditStatisticEvent> {

    private RedditStorage redditStorage;

    @Override
    public void onApplicationEvent(RedditStatisticEvent inboundSseEvent) {
        redditStorage.save(new SubredditStreamData(inboundSseEvent.getData(),
                inboundSseEvent.getName()));
    }

}
