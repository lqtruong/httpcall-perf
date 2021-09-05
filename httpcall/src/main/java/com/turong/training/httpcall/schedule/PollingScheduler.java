package com.turong.training.httpcall.schedule;

import com.turong.training.httpcall.entity.Poll;
import com.turong.training.httpcall.service.PollBatchService;
import com.turong.training.httpcall.service.PollService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class PollingScheduler {

    @Value("${httpcall.poll-api:}")
    private String url;

    @Value("${httpcall.scheduled.recordsPerJob:1000}")
    private int recordsPerJob;

    @Value("${httpcall.scheduled.maxRecordsPerJob:2000}")
    private int maxRecordsPerJob;

    @Value("${httpcall.scheduled.paging.enabled:true}")
    private boolean pagingEnabled;

    @Value("${httpcall.scheduled.paging.rows:25}")
    private int pagingRows;

    @Value("${httpcall.scheduled.daysToUpdate:25}")
    private int daysToUpdate;

    @Autowired
    @Qualifier("httpPoolExecutor")
    private ThreadPoolTaskExecutor httpPoolExecutor;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PollService pollService;

    @Autowired
    private PollBatchService pollBatchService;

    @Autowired
    private PollingUpdateProducer producer;

    private final Deque<PollingTableMetric> pollingTables = new ArrayDeque<>();

    @Scheduled(cron = "${httpcall.scheduled.cron:0 */10 * * * *}")
    public void doPoll() {
        log.info("Start to poll, active count:{}", httpPoolExecutor.getActiveCount());
        refreshTableQueue();

        // check if the scheduled job was organized by paging
        int total = 0;
        int realRecordsPerJob = Math.min(recordsPerJob, maxRecordsPerJob);
        do {
            if (pagingEnabled) {
                for (int offset = 0; ; offset++) {
                    PollingTableMetric tableMetric = pickTable();
                    if (Objects.isNull(tableMetric)) {
                        log.info("Finished all tables. Stop the job.");
                        break;
                    }
                    String pollingTable = tableMetric.getTable();
                    String pollStatus = "SENT";
                    List<Poll> polls = pollService.getPolls(pollingTable, pollStatus, offset, pagingRows);
                    if (CollectionUtils.isEmpty(polls)) {
                        // no records found in the picked table, select another table
                        log.info("Finished table:{}, cost:{}ms, removed table:{} from queue.",
                                pollingTable,
                                Duration.between(tableMetric.getStart(), Instant.now()).toMillis(),
                                pollingTables.pop());
                        break;
                    }
                    httpPoolExecutor.submit(
                            HttpCallTask.builder()
                                    .url(url)
                                    .pollsToUpdate(polls)
                                    .table(pollingTable)
                                    .restTemplate(restTemplate)
                                    .taskOptions(
                                            TaskOptions.builder().producer(producer).build()
                                    )
                                    .build()
                    );
                    total += CollectionUtils.size(polls);
                    if (total >= realRecordsPerJob) {
                        break;
                    }
                }
            } else {
                PollingTableMetric tableMetric = pickTable();
                if (Objects.isNull(tableMetric)) {
                    log.info("Finished all tables. Stop the job.");
                    break;
                }
                String pollingTable = tableMetric.getTable();
                String pollStatus = "SENT";
                // fetch {{limit}} not updated records from MySQL, send to external endpoint
                List<Poll> polls = pollService.getPolls(pollingTable, pollStatus, 0, realRecordsPerJob);
                if (CollectionUtils.isEmpty(polls)) {
                    // no records found in the picked table, select another table
                    log.info("Finished table:{}, cost:{}ms, removed table:{} from queue.",
                            pollingTable,
                            Duration.between(tableMetric.getStart(), Instant.now()).toMillis(),
                            pollingTables.pop());
                }
                httpPoolExecutor.submit(
                        HttpCallTask.builder()
                                .url(url)
                                .pollsToUpdate(polls)
                                .table(pollingTable)
                                .restTemplate(restTemplate)
                                .taskOptions(
                                        TaskOptions.builder().producer(producer).build()
                                )
                                .build()
                );
                total += CollectionUtils.size(polls);
            }
            log.info("pollingTables size={}, total updated:{}", pollingTables.size(), total);
        } while (!pollingTables.isEmpty() && total < recordsPerJob);
    }

    private PollingTableMetric pickTable() {
        if (pollingTables.isEmpty()) {
            return null;
        }
        PollingTableMetric tableMetric = pollingTables.peek();
        if (Objects.isNull(tableMetric.getStart())) {
            tableMetric.setStart(Instant.now());
        }
        return tableMetric;
    }

    private void refreshTableQueue() {
        if (pollingTables.isEmpty()) {
            List<String> allPollingTables = pollBatchService.getAllPollingTables(daysToUpdate);
            log.info("Found polling tables:{}", allPollingTables);
            if (CollectionUtils.isEmpty(allPollingTables)) {
                return;
            }
            allPollingTables.stream().forEach(table -> pollingTables.push(PollingTableMetric.of(table)));
        }
    }

}
