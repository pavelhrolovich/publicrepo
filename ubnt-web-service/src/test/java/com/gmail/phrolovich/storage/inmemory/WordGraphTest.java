package com.gmail.phrolovich.storage.inmemory;


import com.gmail.phrolovich.storage.SubredditStatistic;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class WordGraphTest {
    private WordGraph storage;

    @Before
    public void setUp() throws Exception {
        storage = new WordGraph();
    }

    @Test
    public void shouldAddFirstWorkToChart() {
        storage.save("SOME");
        assertEquals(1, storage.getWordStats("SOME"));
        assertEquals(0, storage.getWordStats("SomeOther"));
    }

    @Test
    public void shouldClearStorage() {
        storage.save("SOME");
        assertEquals(1, storage.getWordStats("SOME"));
        assertEquals(0, storage.getWordStats("SomeOther"));

        storage.clear();
        assertEquals(0, storage.getSize());
        assertEquals(0, storage.getWordStats("SOME"));
    }

    @Test
    public void shouldCalculateStatisticForMultiplyHits() {
        storage.save("SOME");
        storage.save("SOME");
        storage.save("SOME");
        storage.save("Other");
        storage.save("Other");
        assertEquals(3, storage.getWordStats("SOME"));
        assertEquals(2, storage.getWordStats("other"));
    }

    @Test
    public void shouldCalculateChartTop() {
        storage.save("One");
        storage.save("Two");
        storage.save("Two");
        storage.save("Three");
        storage.save("Three");
        storage.save("Three");
        storage.save("Four");
        storage.save("Four");
        storage.save("Four");
        storage.save("Four");
        storage.save("Five");
        storage.save("Five");
        storage.save("Five");
        storage.save("Five");
        storage.save("Five");

        assertEquals(5, storage.getWordStats("Five"));
        assertEquals(4, storage.getWordStats("Four"));
        assertEquals(3, storage.getWordStats("Three"));
        assertEquals(2, storage.getWordStats("Two"));
        assertEquals(1, storage.getWordStats("One"));

        List<SubredditStatistic> chart = storage.getMostPopularWordsPerSlice(-1, 1);
        assertEquals(5, chart.size());
        assertEquals(5, chart.get(0).getCount());
        assertEquals("five", chart.get(0).getName());
        storage.adjustSlices();
        storage.adjustSlices();

        chart = storage.getMostPopularWordsPerSlice(1, 1);
        assertEquals(5, chart.size());
        assertEquals(0, chart.get(0).getCount());
        assertEquals(0, chart.get(1).getCount());
        assertEquals(0, chart.get(2).getCount());
        assertEquals(0, chart.get(3).getCount());
        assertEquals(0, chart.get(4).getCount());
    }

    @Test
    public void shouldStoreStatisticPerSlides() throws InterruptedException {
        storage.save("SOME");
        storage.save("SOME");
        storage.save("SOME");
        storage.adjustSlices();
        storage.save("SOME");
        assertEquals(4, storage.getWordStats("SOME"));

        assertEquals(3, storage.getStatsForSlice(1));
        assertEquals(3, storage.getStatsForSlice(2));
        assertEquals(4, storage.getStatsForSlice(-1));
        storage.adjustSlices();
        storage.save("SOME");

        assertEquals(1, storage.getStatsForSlice(1));
        assertEquals(4, storage.getStatsForSlice(2));
        assertEquals(4, storage.getStatsForSlice(3));
        assertEquals(4, storage.getStatsForSlice(10));
        assertEquals(5, storage.getStatsForSlice(-1));

        storage.adjustSlices();

        assertEquals(1, storage.getStatsForSlice(1));
        assertEquals(2, storage.getStatsForSlice(2));
        assertEquals(5, storage.getStatsForSlice(3));
        assertEquals(5, storage.getStatsForSlice(10));
        assertEquals(5, storage.getStatsForSlice(-1));

        storage.adjustSlices();

        assertEquals(0, storage.getStatsForSlice(1));
        assertEquals(1, storage.getStatsForSlice(2));
        assertEquals(2, storage.getStatsForSlice(3));
        assertEquals(5, storage.getStatsForSlice(10));
        assertEquals(5, storage.getStatsForSlice(-1));
    }

    @Test
    public void shouldStoreStatsPerParent() {
        storage.save("SOME");
        storage.save("SOME");
        storage.save("SOME");
        storage.save("SOfE");
        storage.save("Other");
        storage.save("Other");
        assertEquals(3, storage.getWordStats("SOME"));
        assertEquals(1, storage.getWordStats("SOfE"));
        assertEquals(2, storage.getWordStats("other"));
    }

    @Test
    public void shouldSupportOneCharacterNodes() {
        storage.save("S");
        storage.adjustSlices();
        assertEquals(1, storage.getWordStats("S"));
        assertEquals(0, storage.getWordStats("F"));
    }
}