package com.gmail.phrolovich.integration;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = false)
public class RedditStatisticEvent extends ApplicationEvent {
    private String data;
    private String name;

    public RedditStatisticEvent(Object source, String name, String data) {
        super(source);
        this.data = data;
        this.name = name;
    }
}
