package com.turong.training.httpcall.service;

import com.turong.training.httpcall.entity.PollBatch;

import java.util.List;

public interface PollBatchService {

    int generatePollsBatchRecords();

    List<PollBatch> getAll();

    List<String> getAllPollingTables(int days);

}
