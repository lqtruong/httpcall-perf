package com.turong.training.httpcall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.turong.training.httpcall.entity.Poll;

import java.util.List;

public interface PollService {

    List<Poll> getPolls(String pollingTable, String pollStatus, int offset, int pagingRows);

    int generatePollsRecordsByDay();

    IPage<Poll> getPollsPaged(String pollingTable, String pollStatus, int offset, int pagingRows);

    int updateBatch(String table, List<Poll> polls);

}
