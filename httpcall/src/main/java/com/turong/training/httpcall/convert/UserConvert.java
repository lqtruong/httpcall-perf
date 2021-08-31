package com.turong.training.httpcall.convert;

import com.turong.training.httpcall.controller.UserSaveRequest;
import com.turong.training.httpcall.controller.UserSearchRequest;
import com.turong.training.httpcall.controller.UserResponse;
import com.turong.training.httpcall.entity.User;
import com.turong.training.httpcall.service.UserSearchCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserConvert {

    @Mapping(source = "tenant", target = "tenantId")
    User toUser(final UserSaveRequest userSaveRequest);

    @Mapping(source = "tenantId", target = "tenant")
    UserResponse toResponse(final User user);

    UserSearchCriteria toSearchCriteria(UserSearchRequest searchRequest);
}
