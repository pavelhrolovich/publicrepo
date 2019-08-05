package com.gmail.phrolovich.controller;

import com.gmail.phrolovich.api.RedditApi;
import com.gmail.phrolovich.api.RedditStatistic;
import com.gmail.phrolovich.api.TimePeriod;
import com.gmail.phrolovich.storage.RedditStorage;
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
    private final RedditStorage storage;

    @Override
    public int statisticOverTime(TimePeriod time) {
        return storage.getSubredditsAndCommentCount(time.getSliceCount());
    }

    @Override
    public List<RedditStatistic> listTop100MostActiveSubreddits(TimePeriod time) {
        return listMostActiveSubreddits(time, 1);
    }

    @Override
    public List<RedditStatistic> listMostActiveSubreddits(TimePeriod time, int page) {
        List<SubredditStatistic> result = storage.getMostPopularSubreddits(time.getSliceCount(), page);
        return result.stream()
                .map(subredditStatistic -> new RedditStatistic(subredditStatistic.getName(), subredditStatistic.getCount()))
                .collect(Collectors.toList());
    }
}
