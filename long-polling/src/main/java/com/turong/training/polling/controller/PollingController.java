package com.turong.training.polling.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class PollingController {

    @GetMapping("/long-polling")
    public ResponseEntity<UserResponse> polling() {
        log.debug("Long polling");
        UserResponse response = new UserResponse();
        Random rd = new Random();
        response.setUsername(RandomStringUtils.random(9));
        response.setEmail(StringUtils.join(RandomStringUtils.random(9), "@mail", ".com"));
        response.setFeedback(RandomStringUtils.random(30));
        return ResponseEntity.ok(response);
    }

}
