package com.turong.training.httpcall.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turong.training.httpcall.entity.Poll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollingResult {

    private long id;
    private String poll;
    private String pollStatus;
    private String reason;
    private String table;

    public static Poll toPoll(PollingResult response) {
        Poll poll = new Poll();
        if (!isBlank(response.getPoll())) {
            poll.setPoll(response.getPoll());
        }
        if (!isBlank(response.getPollStatus())) {
            poll.setPollStatus(response.getPollStatus());
        }
        if (!isBlank(response.getReason())) {
            poll.setReason(response.getReason());
        }
        poll.setId(response.getId());
        return poll;
    }

}
