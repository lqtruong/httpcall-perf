package com.turong.training.httpcall.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

@Component
@Slf4j
public class PollingScheduler {

    @Value("${rest.poll-api:}")
    private String url;

    @Autowired
    @Qualifier("httpPoolExecutor")
    private ThreadPoolTaskExecutor httpPoolExecutor;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "${scheduled.cron:0 */10 * * * *}")
    public void doPoll() {
        log.info("Start to poll, active count:{}", httpPoolExecutor.getActiveCount());

        // fetch 1k not updated records from MySQL, send to external endpoint
        IntStream.range(0, 1000).forEach(i -> httpPoolExecutor.submit(new HttpCallTask(url, restTemplate)));
    }

}
