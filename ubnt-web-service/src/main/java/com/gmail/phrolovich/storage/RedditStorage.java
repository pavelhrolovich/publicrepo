package com.gmail.phrolovich.storage;

import java.util.List;

public interface RedditStorage {

    /**
     * Store the Reddit stream event: comment or subreddit
     * @param subredditStreamData - reddit data to store
     */
    void save(SubredditStreamData subredditStreamData);

    /**
     * Retrivies statistic for time slice
     * @param sliceCount - amount of slices starting from now. Each slide - 1 minute
     * @return subreddit statistic
     */
    int getSubredditsAndCommentCount(int sliceCount);

    /**
     * The active subreddits. (`activity =
     * submissions + comments`)
     * @param sliceCount - amount of time intervals to use in calculation (minutes starting from now)
     * @param page - requested page
     * @return list of statistic entries on a given page
     */
    List<SubredditStatistic> getMostPopularSubreddits(int sliceCount, int page);

    /**
     * Reset the storage (clear), e.g. clear all the data
     */
    void clear();

    /**
     * Force storage to adjust time slices. This method should not be used anywhere except for technical purpose
     */
    void forceAdjustSlices();
}
