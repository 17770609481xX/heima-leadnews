package com.heima.login.service.impl;

import com.heima.login.service.ApUserLoginService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mappers.app.ApUserMapper;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.jwt.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class ApUserLoginServiceImpl implements ApUserLoginService {

    @Autowired
    private ApUserMapper apUserMapper;

    //用户登录验证
    @Override
    public ResponseResult loginAuth(ApUser user) {
        //验证参数
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //查询用户
        ApUser apUser = apUserMapper.selectByApPhone(user.getPhone());
        if (apUser==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        //密码错误
        if (!user.getPassword().equals(apUser.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        apUser.setPassword("");
        Map<String,Object> map = new HashMap<>();
        //jwt鉴权
        map.put("token", AppJwtUtil.getToken(apUser));
        map.put("user",apUser);
        return ResponseResult.okResult(map);
    }
}