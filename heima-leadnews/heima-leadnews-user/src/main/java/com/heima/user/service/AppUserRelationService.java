package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;

public interface AppUserRelationService {

    //用户的关注或者取消关注
    public ResponseResult follow(UserRelationDto dto);
}
