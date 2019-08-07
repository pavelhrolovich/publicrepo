package com.gmail.phrolovich.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SubredditStreamData {
    private String data;
    private String name;
}
