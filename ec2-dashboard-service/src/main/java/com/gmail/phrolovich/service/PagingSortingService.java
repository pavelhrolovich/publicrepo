package com.gmail.phrolovich.service;

import com.gmail.phrolovich.api.dto.Direction;
import com.gmail.phrolovich.integration.AWSInstanceData;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PagingSortingService {
    private static Map<String, Comparator<AWSInstanceData>> COMPARATOR_MAP = new HashMap<>();

    static {
        COMPARATOR_MAP.put("name", Comparator.comparing(AWSInstanceData::getName).thenComparing(AWSInstanceData::getInstanceId));
        COMPARATOR_MAP.put("instanceid", Comparator.comparing(AWSInstanceData::getInstanceId).thenComparing(AWSInstanceData::getInstanceId));
        COMPARATOR_MAP.put("instancetype", Comparator.comparing(AWSInstanceData::getInstanceType).thenComparing(AWSInstanceData::getInstanceId));
        COMPARATOR_MAP.put("state", Comparator.comparing(AWSInstanceData::getState).thenComparing(AWSInstanceData::getInstanceId));
        COMPARATOR_MAP.put("availabilityzone", Comparator.comparing(AWSInstanceData::getAvailabilityZone).thenComparing(AWSInstanceData::getInstanceId));
        COMPARATOR_MAP.put("privateipaddress", Comparator.comparing(AWSInstanceData::getPrivateIpAddress).thenComparing(AWSInstanceData::getInstanceId));
        COMPARATOR_MAP.put("publicipaddress", Comparator.comparing(AWSInstanceData::getPublicIpAddress).thenComparing(AWSInstanceData::getInstanceId));
    }

    public List<AWSInstanceData> sortAndPage(int page, int itemsPerPage, String fieldName, Direction direction, List<AWSInstanceData> items) {
        Comparator<AWSInstanceData> serverInstanceComparator = COMPARATOR_MAP.get(fieldName.toLowerCase());
        if (serverInstanceComparator == null) {
            throw new IllegalArgumentException("Field is not supported: " + fieldName);
        }
        if (Direction.DESC == direction) {
            items.sort(serverInstanceComparator.reversed());
        } else {
            items.sort(serverInstanceComparator);
        }
        if (items.size() < ((page - 1) * itemsPerPage)) {
            throw new IllegalArgumentException("Page is not found: " + page);
        }

        int from = (page - 1) * itemsPerPage;
        int to = Math.min(from + itemsPerPage, items.size());

        return items.subList(from, to);
    }


}

