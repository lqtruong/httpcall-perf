package com.turong.training.httpcall.schedule;

import com.turong.training.httpcall.service.PollService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PollingUpdateConsumer {

    @Autowired
    private PollService pollService;

    @KafkaListener(
            topics = "httpcall-poll-update",
            groupId = "httpcall-poll-update-group",
            containerFactory = "pollingMessageKafkaFactory"
    )
    public void receivePing(@Payload List<PollingResult> results) {
        if (CollectionUtils.isEmpty(results)) {
            log.info("no message in the batch");
            return;
        }
        // write to db
        results.stream()
                .filter(m -> StringUtils.isNotBlank(m.getTable()))
                .collect(Collectors.groupingBy(PollingResult::getTable))
                .entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().stream().map(PollingResult::toPoll).collect(Collectors.toList())))
                .entrySet().stream().forEach(e -> {
            log.info("Update table:{}, values:{}", e.getKey(), e.getValue());
            pollService.updateBatch(e.getKey(), e.getValue());
        });
    }

}
