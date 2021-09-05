package com.turong.training.httpcall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turong.training.httpcall.entity.Poll;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PollMapper extends BaseMapper<Poll> {

    int insertPollsByDay(@Param("table") String table, @Param("polls") List<Poll> polls);

    int countAll(@Param("table") String table, @Param("pollStatus") String pollStatus);

    int deleteAll(@Param("table") String table);

    List<Poll> selectAll(@Param("table") String pollingTable, @Param("pollStatus") String pollStatus, @Param("offset") int offset, @Param("rows") int pagingRows);

    int updateBatch(@Param("table") String table, @Param("polls") List<Poll> polls);

}
