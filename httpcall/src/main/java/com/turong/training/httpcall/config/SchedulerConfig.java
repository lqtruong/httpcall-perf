package com.turong.training.httpcall.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulerConfig {

    @Bean
    @ConfigurationProperties(prefix = "scheduled.pool")
    public ThreadPoolConfig threadPoolConfig() {
        return new ThreadPoolConfig();
    }

    @Bean(name = "httpPoolExecutor")
    public ThreadPoolTaskExecutor httpPoolExecutor(@Autowired ThreadPoolConfig threadPoolConfig) {
        log.info("threadPoolConfig:{}", threadPoolConfig);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolConfig.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolConfig.getMaxPoolSize());
        // executor.setQueueCapacity(threadPoolConfig.getQueueCapacity());
        executor.setThreadNamePrefix("httpLongPollingExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        return template;
    }

    @Getter
    @Setter
    @ToString
    static class ThreadPoolConfig {

        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;

    }

}
