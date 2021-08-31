package com.turong.training.polling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("users")
@Accessors(chain = true)
public class User {

    @Id
    private Long id;
    private String username;
    private String email;
    private String tenantId;
    private String feedback;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
