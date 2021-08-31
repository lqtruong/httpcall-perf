package com.turong.training.polling.convert;

import com.turong.training.polling.controller.UserSaveRequest;
import com.turong.training.polling.controller.UserSearchRequest;
import com.turong.training.polling.controller.UserResponse;
import com.turong.training.polling.entity.User;
import com.turong.training.polling.service.UserSearchCriteria;
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
