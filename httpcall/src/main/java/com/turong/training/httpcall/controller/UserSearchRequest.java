package com.turong.training.httpcall.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.turong.training.httpcall.web.vo.PagingRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSearchRequest extends PagingRequest {

    private String name;

}
