package com.turong.training.httpcall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.turong.training.httpcall.entity.Poll;
import com.turong.training.httpcall.mapper.PollMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class PollServiceTest {

    @Autowired
    private PollService pollService;

    @Autowired
    private PollMapper pollMapper;

    @BeforeEach
//    @Disabled
    public void clearAllPolls() {
         IntStream.rangeClosed(1, 10)
             .forEach(i -> pollMapper.deleteAll("polls_" + i));
    }

    @Test
//    @Disabled
    public void generatePollsBatchRecords() {
        pollService.generatePollsRecordsByDay();
        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    assertThat(
                            pollMapper.countAll("polls_" + i, null),
                            greaterThan(199));
                });
    }

    @Test
    public void getPolls() {

        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    List<Poll> polls = pollService.getPolls("polls_" + i, "SENT", 1, 50);
                    assertThat(polls, hasSize(greaterThan(0)));
                    assertThat(polls, hasSize(lessThan(500)));
                });
    }

    @Test
    public void getPollsPaged() {
        IPage<Poll> polls = pollService.getPollsPaged("polls_1", "SENT", 2, 7);
        assertThat(polls.getRecords(), hasSize(7));

        polls = pollService.getPollsPaged("polls_1", "SENT", 2, 32);
        assertThat(polls.getRecords(), hasSize(1));

        polls = pollService.getPollsPaged("polls_1", "SENT", 2, 50);
        assertThat(polls.getRecords(), hasSize(0));

    }

}
