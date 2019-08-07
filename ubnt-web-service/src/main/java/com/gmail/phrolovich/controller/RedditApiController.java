package com.gmail.phrolovich.controller;

import com.gmail.phrolovich.api.RedditApi;
import com.gmail.phrolovich.api.RedditResponse;
import com.gmail.phrolovich.api.RedditApiTimeInterval;
import com.gmail.phrolovich.storage.SubredditStatisticService;
import com.gmail.phrolovich.storage.SubredditStatistic;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RedditApiController implements RedditApi {
    @Autowired
    private final SubredditStatisticService service;

    @Override
    public int statisticOverTime(RedditApiTimeInterval time) {
        return service.getSubredditsAndCommentCount(time.getSlicesPerPeriod());
    }

    @Override
    public List<RedditResponse> listTop100MostActiveSubreddits(RedditApiTimeInterval time) {
        return listMostActiveSubreddits(time, 1);
    }

    @Override
    public List<RedditResponse> listMostActiveSubreddits(RedditApiTimeInterval time, int page) {
        List<SubredditStatistic> result = service.getMostPopularSubreddits(time.getSlicesPerPeriod(), page);
        return result.stream()
                .map(subredditStatistic -> new RedditResponse(subredditStatistic.getName(), subredditStatistic.getCount()))
                .collect(Collectors.toList());
    }
}
