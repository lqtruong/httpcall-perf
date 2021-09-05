package com.turong.training.httpcall.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PollingUpdateProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.topics.poll-update:httpcall-poll-update}")
    private String httpcallPollUpdateTopic;

    public void sendToKafka(PollingResult result) {
        Message<?> message = MessageBuilder
                .withPayload(result)
                .setHeader(KafkaHeaders.TOPIC, httpcallPollUpdateTopic)
                .build();
        kafkaTemplate.send(message);
    }

}
