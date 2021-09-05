package com.turong.training.httpcall.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@Slf4j
public class PollBatchServiceTest {

    @Autowired
    private PollBatchService pollBatchService;

    @Test
    @Disabled("not overriden")
    public void generatePollsBatchRecords() {
        pollBatchService.generatePollsBatchRecords();
        assertThat(pollBatchService.getAll(), hasSize(10));
    }

    @Test
    public void getAllPollingTables_5days() {
        int days = 5;
        List<String> tables = pollBatchService.getAllPollingTables(days);
        log.info("Found tables previous {} days: {}", days, tables);

        assertThat(tables, hasSize(1));
    }

    @Test
    public void getAllPollingTables_11days() {
        int days = 11;
        List<String> tables = pollBatchService.getAllPollingTables(days);
        log.info("Found tables previous {} days: {}", days, tables);
        assertThat(tables, hasSize(3));
    }

    @Test
    public void getAllPollingTables_25days() {
        int days = 25;
        List<String> tables = pollBatchService.getAllPollingTables(days);
        log.info("Found tables previous {} days: {}", days, tables);
        assertThat(tables, hasSize(8));
    }

}
