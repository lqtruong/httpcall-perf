package com.turong.training.httpcall.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollingResponse {

    private long id;
    private String username;
    private String email;
    private String tenant;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
