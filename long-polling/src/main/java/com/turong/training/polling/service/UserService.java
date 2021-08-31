package com.turong.training.polling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.turong.training.polling.entity.User;

import java.util.List;

public interface UserService {

    User getUser(String id);

    User create(User userToCreate);

    int deleteByEmail(String email);

    int deleteById(String id);

    List<User> getAll();

    int saveBatch(List<User> usersToCreate);

    IPage<User> searchPagedUsers(IPage<User> paged, UserSearchCriteria searchCriteria);

    int updateBatch(List<User> usersToUpdate);

}
