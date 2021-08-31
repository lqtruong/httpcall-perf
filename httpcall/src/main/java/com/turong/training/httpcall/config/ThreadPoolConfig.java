package com.turong.training.httpcall.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ThreadPoolConfig {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;

}
