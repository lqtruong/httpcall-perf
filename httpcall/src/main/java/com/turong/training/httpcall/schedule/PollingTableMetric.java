package com.turong.training.httpcall.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString(of = {"table"})
public class PollingTableMetric {

    private String table;
    private Instant start;

    public static PollingTableMetric of(String table) {
        PollingTableMetric metric = new PollingTableMetric();
        metric.setTable(table);
        return metric;
    }

}
