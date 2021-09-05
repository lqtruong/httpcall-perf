package com.turong.training.httpcall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("polls_batch")
public class PollBatch {

    @Id
    private Long id;
    private Long batch;
    private String tenantId;
    private String tableName;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
