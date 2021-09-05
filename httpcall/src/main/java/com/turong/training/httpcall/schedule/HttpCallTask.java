package com.turong.training.httpcall.schedule;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
public class HttpCallTask implements Runnable {

    private final String url;
    private final RestTemplate restTemplate;

    @SneakyThrows
    @Override
    public void run() {
        log.info("Call HTTP={}", url);

        // Thread.sleep(3000L);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            ResponseEntity<PollingResponse> res = restTemplate
                    .exchange(url, HttpMethod.GET, null, PollingResponse.class);

            if (res.hasBody()) {
                log.info("Response body:{}", res.getBody());
            }
        } catch (Exception e) {
            log.error("Hubspot HttpCall task error", e);
        } finally {
            stopWatch.stop();
            log.info("Hubspot HttpCall task end, cost={}ms", stopWatch.getTotalTimeMillis());
        }
    }

}
