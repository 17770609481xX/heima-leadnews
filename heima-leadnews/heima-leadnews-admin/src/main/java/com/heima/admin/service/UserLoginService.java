package com.heima.admin.service;

import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;

public interface UserLoginService {

    ResponseResult login(AdUser user);
}