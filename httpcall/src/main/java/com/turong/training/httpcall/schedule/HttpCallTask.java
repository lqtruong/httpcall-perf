package com.turong.training.httpcall.schedule;

import com.turong.training.httpcall.entity.Poll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.*;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpCallTask implements Runnable {

    private String url;
    private RestTemplate restTemplate;
    private List<Poll> pollsToUpdate;
    private String table;
    private TaskOptions taskOptions;

    @SneakyThrows
    @Override
    public void run() {
        // Thread.sleep(3000L);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            if (CollectionUtils.isEmpty(pollsToUpdate)) {
                return;
            }
            int size = pollsToUpdate.size();
            log.info("Task size:{}", size);


            List<Poll> sortedPolls = pollsToUpdate.stream().sorted(new Comparator<Poll>() {
                @Override
                public int compare(Poll o1, Poll o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            }).collect(Collectors.toList());
            sortedPolls.get(size - 1).getId();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(url)
                    .queryParam("from", String.valueOf(sortedPolls.get(0).getId()))
                    .queryParam("to", String.valueOf(sortedPolls.get(size - 1).getId()));

            log.info("Call url:{}", builder.toUriString());

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<PollingResponse> res = restTemplate
                    .exchange(builder.toUriString(), HttpMethod.GET, entity, PollingResponse.class);


            if (res.hasBody() && !CollectionUtils.isEmpty(res.getBody().getRecords())) {
                final List<PollingResult> results = res.getBody().getRecords();
                List<Long> ids = pollsToUpdate.stream().map(Poll::getId).collect(Collectors.toList());
                List<PollingResult> sent = results.stream()
                        .filter(r -> ids.contains(r.getId())).collect(Collectors.toList());
                sent.stream().forEach(r -> r.setTable(table));
                res.getBody().setRecords(sent);

                log.info("Response body:{}", res.getBody());

                sent.stream().forEach(s -> taskOptions.getProducer().sendToKafka(s));
                ;
            }
        } catch (Exception e) {
            log.error("Hubspot HttpCall task error", e);
        } finally {
            stopWatch.stop();
            log.info("Hubspot HttpCall task end, cost={}ms", stopWatch.getTotalTimeMillis());
        }
    }

}
