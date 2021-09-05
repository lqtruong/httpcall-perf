package com.turong.training.httpcall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.turong.training.httpcall.entity.PollBatch;
import com.turong.training.httpcall.mapper.PollBatchMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class PollBatchServiceImpl implements PollBatchService {

    @Autowired
    private PollBatchMapper pollBatchMapper;

    @Override
    public int generatePollsBatchRecords() {

        Date now = new Date();
        AtomicInteger total = new AtomicInteger();
        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    PollBatch batch = new PollBatch();
                    batch.setBatch(ThreadLocalRandom.current().nextLong());
                    batch.setTableName("polls_" + i);
                    batch.setCreatedAt(Instant
                            .ofEpochMilli(DateUtils.addDays(now, -1 * (int) (Math.random() * 30)).getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());
                    total.addAndGet(pollBatchMapper.insert(batch));
                });

        return total.get();
    }

    @Override
    public List<PollBatch> getAll() {
        return pollBatchMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<String> getAllPollingTables(int days) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("distinct table_name");
        wrapper.orderByDesc("table_name");
        if (days > 0) {
            wrapper.ge("created_at", DateUtils.addDays(new Date(), days * -1));
        }

        return (List<String>) pollBatchMapper.selectObjs(wrapper);
    }

}
