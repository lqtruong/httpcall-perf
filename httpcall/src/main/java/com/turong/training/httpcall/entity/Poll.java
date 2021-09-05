package com.turong.training.httpcall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("polls_1")
public class Poll {

    @Id
    private Long id;
    private String poll;
    private String tenantId;
    private String pollStatus;
    private String reason;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
