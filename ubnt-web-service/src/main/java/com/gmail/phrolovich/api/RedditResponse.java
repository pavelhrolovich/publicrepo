package com.gmail.phrolovich.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedditResponse {
    private String name;
    private int count;
}
