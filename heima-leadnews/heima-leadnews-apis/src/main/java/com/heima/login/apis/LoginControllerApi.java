package com.heima.login.apis;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUser;

public interface LoginControllerApi {

    //用户登录
    public ResponseResult login(ApUser user);
}
