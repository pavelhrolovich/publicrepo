package com.gmail.phrolovich.storage.inmemory;

import com.gmail.phrolovich.storage.SubredditStatistic;
import com.gmail.phrolovich.storage.SubredditStreamData;
import com.gmail.phrolovich.storage.RedditStorage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class InMemoryRedditStorage implements RedditStorage {
    private static final String REDDIT_COMMENT = "rc";
    private static final String REDDIT_SUBMISSION = "rs";
    public static final int ALL_SLICES = -1;

    private final WordGraph graph;

    public InMemoryRedditStorage() {
        this.graph = new WordGraph();
    }

    @Override
    public void save(SubredditStreamData eventData) {
        if (REDDIT_COMMENT.equalsIgnoreCase(eventData.getName()) ||
                REDDIT_SUBMISSION.equalsIgnoreCase(eventData.getName())) {
            JSONObject jsonObject = new JSONObject(eventData.getData());
            String subreddit = jsonObject.getString("subreddit");
            graph.save(subreddit);
        }
    }

    @Override
    public int getSubredditsAndCommentCount(int sliceCount) {
        return graph.getStatsForSlice(sliceCount);
    }

    @Override
    public List<SubredditStatistic> getMostPopularSubreddits(int sliceCount, int page) {
        return graph.getMostPopularWordsPerSlice(sliceCount, page);
    }

    @Override
    public void clear() {
        graph.clear();
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void forceAdjustSlices() {
        graph.adjustSlices();
    }

    @Scheduled(fixedRate = 60000, initialDelay = 60000)
    public void printStats() {
        log.info("Storage size: " + graph.getSize());
        int max = 100;
        List<SubredditStatistic> chart = graph.getMostPopularWordsPerSlice(ALL_SLICES, 1);
        Iterator<SubredditStatistic> iterator = chart.iterator();
        while (iterator.hasNext() && max > 0) {
            SubredditStatistic currentChartPage = iterator.next();
            String next = currentChartPage.getName();
            log.info((101 - max) + ": " + next+ "(" + currentChartPage.getCount() + ")");
            max--;
        }
    }

}
