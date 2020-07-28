package com.heima.admin.apis;

import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginControllerApi{
    public ResponseResult login( AdUser user);
}