package com.turong.training.polling.controller;

import com.turong.training.polling.web.api.PingControllerApi;
import com.turong.training.polling.web.vo.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

@RestController
public class PingController implements PingControllerApi {

    @Override
    public ResponseEntity<PingResponse> createPing(PingSaveRequest info) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResponse> deletePing(Integer id) {
        BaseResponse okRes = new BaseResponse();
        okRes.setStatus(BaseResponse.StatusEnum.OK);
        return ResponseEntity.ok(okRes);
    }

    @Override
    public ResponseEntity<PingResponse> getById(String id) {
        PingResponse res = new PingResponse();
        res.setPinId("id-" + id);
        res.setId(Long.valueOf(id));
        res.setVal(RandomStringUtils.randomAlphabetic(10));
        res.setCreatedAt(LocalDateTime.now());
        res.setModifiedAt(LocalDateTime.now());
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<PingSearchResponse> searchPings(PingSearchRequest request) {
        PingResponse res = new PingResponse();
        res.setPinId("id-" + RandomStringUtils.random(4));
        res.setId(new Random().nextLong());
        res.setVal(RandomStringUtils.randomAlphabetic(10));
        res.setCreatedAt(LocalDateTime.now());
        res.setModifiedAt(LocalDateTime.now());
        PingSearchResponse searchResponse = new PingSearchResponse();
        searchResponse.setData(Arrays.asList(res));
        searchResponse.setTotal((long) searchResponse.getData().size());
        searchResponse.setStatus(PingSearchResponse.StatusEnum.OK);
        return ResponseEntity.ok(searchResponse);
    }

    @Override
    public ResponseEntity<BaseResponse> updatePing(PingSaveRequest info) {
        return null;
    }

}
