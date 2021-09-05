package com.turong.training.httpcall.schedule;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskOptions {

    @Value("${httpcall.scheduled.http.limit:50}")
    private int httpLimit;

    private PollingUpdateProducer producer;

}
