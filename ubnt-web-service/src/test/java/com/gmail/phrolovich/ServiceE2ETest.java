package com.gmail.phrolovich;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.phrolovich.api.RedditStatistic;
import com.gmail.phrolovich.integration.PushshiftHttpEventStreamService;
import com.gmail.phrolovich.integration.RedditStatisticEvent;
import com.gmail.phrolovich.storage.RedditStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Category(E2ETest.class)
public class ServiceE2ETest {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private RedditStorage storage;
    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private PushshiftHttpEventStreamService service; // disable real http event service

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        storage.clear();
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnStatsForOneRecord() throws Exception {
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("period", "ALL");
        ResultActions result = mockMvc.perform(get("/api/reddit/v1/all")
                .params(params)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        byte[] contentAsByteArray = result.andReturn().getResponse().getContentAsByteArray();
        String count = new String(contentAsByteArray);
        Assert.assertEquals("1", count);
    }

    @Test
    public void shouldCombineCommentsAndSubreddits() throws Exception {
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name2\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name5\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));

        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name5\"}"));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("period", "ALL");
        ResultActions result = mockMvc.perform(get("/api/reddit/v1/all")
                .params(params)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        byte[] contentAsByteArray = result.andReturn().getResponse().getContentAsByteArray();
        String count = new String(contentAsByteArray);
        Assert.assertEquals("15", count);
    }

    @Test
    public void shouldReturnMostPopularRedditsForAllPeriods() throws Exception {
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name2\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name5\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));

        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name5\"}"));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("period", "ALL");
        ResultActions result = mockMvc.perform(get("/api/reddit/v1/top100")
                .params(params)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        RedditStatistic[] list = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsByteArray(), RedditStatistic[].class);
        Assert.assertNotNull(list);
        Assert.assertEquals(6, list.length);

        Assert.assertEquals("name", list[0].getName());
        Assert.assertEquals(7, list[0].getCount());
        Assert.assertEquals("name3", list[1].getName());
        Assert.assertEquals(6, list[1].getCount());
        Assert.assertEquals("name4", list[2].getName());
        Assert.assertEquals(4, list[2].getCount());
        Assert.assertEquals("name1", list[3].getName());
        Assert.assertEquals(3, list[3].getCount());
        Assert.assertEquals("name5", list[4].getName());
        Assert.assertEquals(2, list[4].getCount());
        Assert.assertEquals("name2", list[5].getName());
        Assert.assertEquals(1, list[5].getCount());
    }

    @Test
    public void shouldReturnMostPopularRedditsForLastMinute() throws Exception {
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name2\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name5\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rs", "{\"subreddit\":\"name\"}"));

        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name1\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name5\"}"));

        storage.forceAdjustSlices();
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name3\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name4\"}"));
        applicationEventPublisher.publishEvent(new RedditStatisticEvent(this, "rc", "{\"subreddit\":\"name5\"}"));
        storage.forceAdjustSlices();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("period", "ONE_MINUTE");
        ResultActions result = mockMvc.perform(get("/api/reddit/v1/top?period=ONE_MINUTE")
                .param("period", "ONE_MINUTE")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        RedditStatistic[] list = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsByteArray(), RedditStatistic[].class);
        Assert.assertNotNull(list);
        Assert.assertEquals(6, list.length);

        Assert.assertEquals("name3", list[0].getName());
        Assert.assertEquals(5, list[0].getCount());
        Assert.assertEquals("name4", list[1].getName());
        Assert.assertEquals(3, list[1].getCount());
        Assert.assertEquals("name5", list[2].getName());
        Assert.assertEquals(1, list[2].getCount());
    }
}
