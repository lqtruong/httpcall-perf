package com.turong.training.polling.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.turong.training.polling.entity.User;
import com.turong.training.polling.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(@Autowired final UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUser(String id) {
        Optional<User> user = userMapper.findUser(id);
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

    @Override
    public User create(User userToCreate) {
        final int count = userMapper.insert(userToCreate);
        if (count == 0) {
            return null;
        }
        Optional<User> user = userMapper.findUserByEmail(userToCreate.getEmail());
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

    @Override
    public int deleteByEmail(String email) {
        return userMapper.deleteByEmail(email);
    }

    @Override
    public int deleteById(String id) {
        return userMapper.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        QueryWrapper<User> query = Wrappers.query();
        return userMapper.selectList(query);
    }

    @Override
    public int saveBatch(List<User> usersToCreate) {
        return userMapper.insertBatch(usersToCreate);
    }

    @Override
    public IPage<User> searchPagedUsers(IPage<User> paged, UserSearchCriteria searchCriteria) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.like("username", searchCriteria.getName());
        return userMapper.selectPage(paged, wrapper);
    }

    @Override
    public int updateBatch(List<User> usersToUpdate) {
        return userMapper.updateBatch(usersToUpdate);
    }


}
