package com.turong.training.httpcall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.turong.training.httpcall.entity.Poll;
import com.turong.training.httpcall.mapper.PollMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@Slf4j
public class PollServiceImpl implements PollService {

    @Autowired
    private PollMapper pollMapper;

    @Override
    public List<Poll> getPolls(String pollingTable, String pollStatus, int offset, int pagingRows) {
        return pollMapper.selectAll(pollingTable, pollStatus, offset, pagingRows);
    }

    @Override
    public int generatePollsRecordsByDay() {
        AtomicInteger total = new AtomicInteger();
        String[] pollStatuses = new String[]{
                "SENT",
                "DROPPED",
                "DEFERRED",
                "DELIVERED",
                "BOUNCE",
                "OPEN",
                "CLICK"
        };
        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    List<Poll> polls = new ArrayList<>();
                    IntStream.rangeClosed(0, ThreadLocalRandom.current().nextInt(200, 500))
                            .forEach(j -> {
                                        Poll poll = new Poll();
                                        poll.setPoll(RandomStringUtils.random(5));
                                        poll.setPollStatus(pollStatuses[(int) (Math.random() * 7)]);
                                        if (StringUtils.equals(poll.getPollStatus(), "DROPPED")) {
                                            poll.setReason("The message was rejected, and no attempt will be made to deliver the message.");
                                        }
                                        if (StringUtils.equals(poll.getPollStatus(), "DEFERRED")) {
                                            poll.setReason(
                                                    "The recipient’s email server has temporarily rejected message, and subsequent attempts will be made to deliver the message.");
                                        }
                                        if (StringUtils.equals(poll.getPollStatus(), "BOUNCE")) {
                                            poll.setReason(
                                                    "The recipient's email server couldn't or wouldn't accept the message, and no further attempts will be made to deliver the message.");
                                        }
                                        polls.add(poll);
                                    }
                            );
                    total.addAndGet(pollMapper.insertPollsByDay("polls_" + i, polls));
                });

        return total.get();
    }

    @Override
    public IPage<Poll> getPollsPaged(String pollingTable, String pollStatus, int offset, int pagingRows) {
        IPage<Poll> page = new Page<>(offset, pagingRows);
        return pollMapper.selectPage(page, Wrappers.<Poll>lambdaQuery().eq(Poll::getPollStatus, pollStatus).orderByAsc(Poll::getCreatedAt));
    }

    @Override
    public int updateBatch(String table, List<Poll> polls) {
        return pollMapper.updateBatch(table, polls);
    }

}
