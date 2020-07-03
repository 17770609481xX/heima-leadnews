package com.heima.user.apis;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.user.pojos.ApUser;

public interface UserRelationControllerApi {
    //关注
    ResponseResult follow(UserRelationDto dto);
}
