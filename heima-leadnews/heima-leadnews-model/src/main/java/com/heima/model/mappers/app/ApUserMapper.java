package com.heima.model.mappers.app;

import com.heima.model.user.pojos.ApUser;

public interface ApUserMapper {

    //定义按照用户ID查询用户信息的方法：
    ApUser selectById(Integer id);
}
