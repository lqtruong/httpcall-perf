package com.turong.training.polling.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class PollingController {

    String[] pollStatuses = new String[]{
            "SENT",
            "DROPPED",
            "DEFERRED",
            "DELIVERED",
            "BOUNCE",
            "OPEN",
            "CLICK"
    };

    @GetMapping("/long-polling")
    public ResponseEntity<PollingResponse> polling(@RequestParam("from") Integer from, @RequestParam("to") Integer to) {
        log.debug("Long polling, from:{}, to:{}", from, to);
        int takeFrom = from - 1;
        int takeTo = to + 1;
        int total = takeTo - takeFrom;

        PollingResponse response = new PollingResponse();
        response.setTotal(total);
        List<Integer> ids = new ArrayList<>();
        response.setRecords(
                IntStream.rangeClosed(1, total)
                        .mapToObj(i -> {
                                    int id = ThreadLocalRandom.current().nextInt(takeFrom, takeTo);
                                    do {
                                        if (ids.contains(id)) {
                                            id = ThreadLocalRandom.current().nextInt(takeFrom, takeTo);
                                        } else {
                                            ids.add(id);
                                        }
                                    } while (!ids.contains(id));
                                    return generatePollingResult(id);
                                }
                        ).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    private PollingResult generatePollingResult(int id) {
        PollingResult pollingResult = new PollingResult();
        pollingResult.setId(id);
        pollingResult.setPoll(RandomStringUtils.random(9));
        pollingResult.setPollStatus(pollStatuses[(int) (Math.random() * 7)]);
        if (StringUtils.equals(pollingResult.getPollStatus(), "DROPPED")) {
            pollingResult.setReason("long-polling, response dropped");
        }
        if (StringUtils.equals(pollingResult.getPollStatus(), "DEFERRED")) {
            pollingResult.setReason("long-polling, response deferred");
        }
        if (StringUtils.equals(pollingResult.getPollStatus(), "BOUNCE")) {
            pollingResult.setReason("long-polling, response bounce");
        }
        return pollingResult;
    }

}
