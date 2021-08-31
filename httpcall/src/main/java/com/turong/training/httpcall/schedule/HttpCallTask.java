package com.turong.training.httpcall.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
public class HttpCallTask implements Runnable {

    private final String url;
    private final RestTemplate restTemplate;

    @Override
    public void run() {
        log.info("Call HTTP={}", url);

        ResponseEntity<PollingResponse> res = restTemplate
                .exchange(url, HttpMethod.GET, null, PollingResponse.class);

        if (res.hasBody()) {
            log.info("Response body:{}", res.getBody());
        }
    }

}
