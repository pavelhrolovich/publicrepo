package com.gmail.phrolovich.integration;

import com.gmail.phrolovich.storage.SubredditStreamData;
import com.gmail.phrolovich.storage.SubredditStatisticService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RedditEventConsumer implements ApplicationListener<RedditStatisticEvent> {

    private SubredditStatisticService subredditStatisticService;

    @Override
    public void onApplicationEvent(RedditStatisticEvent inboundSseEvent) {
        subredditStatisticService.save(new SubredditStreamData(inboundSseEvent.getData(),
                inboundSseEvent.getName()));
    }

}
