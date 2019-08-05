package com.gmail.phrolovich.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/reddit/v1")
@Api(value = "Reddit API", description = "Define API for Reddit web service")
public interface RedditApi {
    public static final int PAGE_SIZE = 50;

    @ApiOperation(value = ("REST endpoint that returns reddit activity - how much submissions and comments " +
            "have been posted in a given time range"))
    @GetMapping("/all")
    int statisticOverTime(@RequestParam("period")  @ApiParam(value = "Period to get statistic for",
            defaultValue = "ALL") TimePeriod period);

    @ApiOperation(value = "REST endpoint that returns the top 100 most active subreddits.")
    @GetMapping("/top100")
    List<RedditStatistic> listTop100MostActiveSubreddits(@RequestParam("period")  @ApiParam(value = "Period to get" +
            " statistic for", defaultValue = "ALL")  TimePeriod period);

    @ApiOperation(value = "REST endpoint that returns the top 100 most active subreddits. The service is paged and " +
            "returns live view of current server stats, e.g. might change results over the time. Refresh happens " +
            "every minute")
    @GetMapping("/top")
    List<RedditStatistic> listMostActiveSubreddits(@RequestParam("period") @ApiParam(value = "Period to get statistic for",
            defaultValue = "ALL")  TimePeriod period,
                                                   @RequestParam(name = "page", defaultValue = "1") @ApiParam(value = "Page " +
                                                           "to  get for", defaultValue = "1") int page);

}