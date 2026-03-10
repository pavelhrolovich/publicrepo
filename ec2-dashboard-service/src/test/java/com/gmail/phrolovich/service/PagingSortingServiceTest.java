package com.gmail.phrolovich.service;

import com.gmail.phrolovich.api.dto.Direction;
import com.gmail.phrolovich.integration.AWSInstanceData;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PagingSortingServiceTest {
    private final PagingSortingService service = new PagingSortingService();


    @Test
    public void shouldSortEmptyResults() {
        List<AWSInstanceData> result = service.sortAndPage(1, 10, "name", Direction.DESC, new LinkedList<>());
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldSplitByPages() {
        List<AWSInstanceData> result = service.sortAndPage(1, 10, "name", Direction.DESC, createList(50));
        assertEquals(10, result.size());
        assertEquals("AWSInstanceData9", result.get(0).getName());

        result = service.sortAndPage(2, 10, "name", Direction.DESC, createList(52));
        assertEquals(10, result.size());

        result = service.sortAndPage(6, 10, "name", Direction.DESC, createList(52));
        assertEquals(2, result.size());
    }

    @Test
    public void shouldThrowErrorIfOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> service.sortAndPage(15, 10, "name", Direction.DESC, createList(50)));
    }

    @Test
    public void shouldSortAscending() {
        List<AWSInstanceData> result = service.sortAndPage(1, 10, "name", Direction.ASC, createList(5));
        assertEquals(5, result.size());
        assertEquals("AWSInstanceData0", result.get(0).getName());
    }

    private List<AWSInstanceData> createList(int count) {
        List<AWSInstanceData> result = IntStream.range(0, count)
                .mapToObj(this::createAwsInstance)
                .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    private AWSInstanceData createAwsInstance(int index) {
        AWSInstanceData awsInstanceData = new AWSInstanceData();
        awsInstanceData.setName("AWSInstanceData" + index);
        awsInstanceData.setState("running");
        awsInstanceData.setInstanceId("instance" + index);
        awsInstanceData.setInstanceType("t2.nano");
        awsInstanceData.setAvailabilityZone("zonea");
        awsInstanceData.setPrivateIpAddress("192.168.0." + index);
        awsInstanceData.setPublicIpAddress("192.168.1." + index);
        return awsInstanceData;
    }


}
