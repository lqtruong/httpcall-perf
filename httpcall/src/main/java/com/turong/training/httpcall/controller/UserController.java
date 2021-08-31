package com.turong.training.httpcall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.turong.training.httpcall.convert.UserConvert;
import com.turong.training.httpcall.entity.User;
import com.turong.training.httpcall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserConvert userConvert;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable final String id) {
        log.debug("Get user with id={}", id);
        return ResponseEntity.ok(userConvert.toResponse(userService.getUser(id)));

    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody final UserSaveRequest userSaveRequest) {
        userValidator.validate(userSaveRequest);
        return ResponseEntity.ok(userConvert.toResponse(
                userService.create(userConvert.toUser(userSaveRequest))));
    }

    @DeleteMapping
    public void deleteUserByEmail(@RequestParam final String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email to delete must be present!");
        }
        userService.deleteByEmail(email);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable final String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Id to delete must be present!");
        }
        userService.deleteById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.debug("Get all users");
        return ResponseEntity.ok(
                userService.getAll().stream().map(userConvert::toResponse).collect(Collectors.toList()));
    }

    @PostMapping("/insert-batch")
    public ResponseEntity<Integer> insertBatch(@RequestBody List<UserSaveRequest> users) {
        log.debug("Insert batch of users={}", users);
        if (CollectionUtils.isEmpty(users)) {
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(userService.saveBatch(users.stream().map(userConvert::toUser).collect(Collectors.toList())));
    }

    @GetMapping("/paged")
    public ResponseEntity<IPage<UserResponse>> getPaged(UserSearchRequest searchRequest) {
        log.debug("Search paged users={}", searchRequest);
        IPage<User> paged = new Page<>(searchRequest.getPage(), searchRequest.getRows());
        paged.orders().add(OrderItem.desc("created_at"));
        paged.orders().add(OrderItem.desc("id"));
        return ResponseEntity.ok(
                userService
                        .searchPagedUsers(paged, userConvert.toSearchCriteria(searchRequest))
                        .convert(userConvert::toResponse));
    }

    @PostMapping("/update-batch")
    public ResponseEntity<Integer> updateBatch(@RequestBody List<UserSaveRequest> users) {
        log.debug("Update batch users={}", users);
        if (CollectionUtils.isEmpty(users)) {
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(
                userService.updateBatch(users.stream().map(userConvert::toUser).collect(Collectors.toList())));
    }

}
