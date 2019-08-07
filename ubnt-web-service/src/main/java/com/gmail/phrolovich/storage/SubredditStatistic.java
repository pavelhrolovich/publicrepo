package com.gmail.phrolovich.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubredditStatistic {
    private String name;
    private int count;
}
