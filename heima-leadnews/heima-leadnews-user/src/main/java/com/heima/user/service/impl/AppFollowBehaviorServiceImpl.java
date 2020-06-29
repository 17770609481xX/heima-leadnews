package com.heima.user.service.impl;

import com.heima.model.behavior.dtos.FollowBehaviorDto;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApFollowBehavior;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mappers.app.ApBehaviorEntryMapper;
import com.heima.model.mappers.app.ApFollowBehaviorMapper;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.service.AppFollowBehaviorService;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class AppFollowBehaviorServiceImpl implements AppFollowBehaviorService {

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private ApFollowBehaviorMapper apFollowBehaviorMapper;

    @Override
    @Async//开启异步请求
    public ResponseResult saveFollowBehavior(FollowBehaviorDto dto) {
        //获取用户信息
        ApUser user = AppThreadLocalUtils.getUser();
        // 用户和设备不能同时为空
        if(user==null&& dto.getEquipmentId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(user!=null){
            userId = user.getId();
        }
        //查询行为实体
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipemntId(userId, dto.getEquipmentId());
        if (apBehaviorEntry==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //保存app关注行为
        ApFollowBehavior alb = new ApFollowBehavior();
        alb.setEntryId(apBehaviorEntry.getId());
        alb.setCreatedTime(new Date());
        alb.setArticleId(dto.getArticleId());
        alb.setFollowId(dto.getFollowId());
        return ResponseResult.okResult(apFollowBehaviorMapper.insert(alb));
    }
}
