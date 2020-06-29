package com.heima.user.service.impl;

import com.heima.common.zookeeper.sequence.Sequences;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.behavior.dtos.FollowBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mappers.app.ApAuthorMapper;
import com.heima.model.mappers.app.ApUserFanMapper;
import com.heima.model.mappers.app.ApUserFollowMapper;
import com.heima.model.mappers.app.ApUserMapper;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserFan;
import com.heima.model.user.pojos.ApUserFollow;
import com.heima.user.config.ZookeeperConfig;
import com.heima.user.service.AppFollowBehaviorService;
import com.heima.user.service.AppUserRelationService;
import com.heima.utils.common.BurstUtils;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class AppUserRelationServiceImpl implements AppUserRelationService {

    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private ApUserFanMapper apUserFanMapper;
    @Autowired
    private Sequences sequences;
    @Autowired
    private ApUserFollowMapper apUserFollowMapper;
    @Autowired
    private AppFollowBehaviorService appFollowBehaviorService;

    @Override
    public ResponseResult follow(UserRelationDto dto) {
        //判断一下要做的操作是否合法
        if (dto.getOperation()==null || dto.getOperation() <0 || dto.getOperation()>1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //获取followid，即文章作者的社交账号id
        Integer followId = dto.getUserId();
        if (followId ==null && dto.getAuthorId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }else if (followId==null){
            ApAuthor apAuthor = apAuthorMapper.selectById(dto.getAuthorId());
            if (apAuthor!=null){
                followId=apAuthor.getUserId().intValue();
            }
        }
        if (followId==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE,"关注人不在");
        }else {
            //获取当前用户
            ApUser user = AppThreadLocalUtils.getUser();
            if(user!=null){
                //判断当前用户是否已经关注
                if (dto.getOperation()==0){
                    //进行关注操作
                    return followByUserId(user,followId,dto.getArticleId());
                }else{
                    //进行取消关注操作
                    return followCancelByUserId(user,followId);
                }
            }else {
                return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            }
        }
    }

    //处理关注的逻辑
    private ResponseResult followByUserId(ApUser user, Integer followId, Integer articleId) {
        //判断要关注的用户账号是否存在
        ApUser apUser = apUserMapper.selectById(followId);
        if (apUser==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE,"关注人不存在");
        }
        ApUserFollow auf = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()),user.getId(),followId);
        if (auf==null){
            //没有关注
            ApUserFan fan = apUserFanMapper.selectByFansId(BurstUtils.groudOne(followId), followId, user.getId());
            if (fan == null) {
                fan = new ApUserFan();
                fan.setId(sequences.sequenceApUserFan());
                fan.setUserId(followId);
                fan.setFansId(user.getId());
                fan.setFansName(user.getName());
                fan.setLevel((short) 0);
                fan.setIsDisplay(true);
                fan.setIsShieldComment(false);
                fan.setIsShieldLetter(false);
                fan.setBurst(BurstUtils.encrypt(fan.getId(), fan.getUserId()));
                apUserFanMapper.insert(fan);
            }
            auf = new ApUserFollow();
            auf.setId(sequences.sequenceApUserFollow());
            auf.setUserId(user.getId());
            auf.setFollowId(followId);
            auf.setFollowName(apUser.getName());
            auf.setCreatedTime(new Date());
            auf.setLevel((short) 0);
            auf.setIsNotice(true);
            auf.setBurst(BurstUtils.encrypt(auf.getId(),auf.getUserId()));
            // 记录关注行为
            FollowBehaviorDto dto = new FollowBehaviorDto();
            dto.setFollowId(followId);
            dto.setArticleId(articleId);
            appFollowBehaviorService.saveFollowBehavior(dto);
            return ResponseResult.okResult(apUserFollowMapper.insert(auf));
        }else {
            //已经关注
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"你已关注，请勿重复关注");
        }
    }

    //处理取消关注的逻辑
    private ResponseResult followCancelByUserId(ApUser user, Integer followId) {
        ApUserFollow auf = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()),user.getId(),followId);
        if(auf==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"未关注");
        }else{
            ApUserFan fan = apUserFanMapper.selectByFansId(BurstUtils.groudOne(followId), followId, user.getId());
            if (fan != null) {
                apUserFanMapper.deleteByFansId(BurstUtils.groudOne(followId), followId, user.getId());
            }
            return ResponseResult.okResult(apUserFollowMapper.deleteByFollowId(BurstUtils.groudOne(user.getId()),user.getId(),followId));
        }
    }


}
