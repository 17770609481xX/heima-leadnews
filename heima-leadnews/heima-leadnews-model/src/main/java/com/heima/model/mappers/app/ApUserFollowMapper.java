package com.heima.model.mappers.app;

import com.heima.model.user.pojos.ApUserFollow;
import org.apache.ibatis.annotations.Param;

public interface ApUserFollowMapper {

    //按照用户ID、关注用户Id查询关注信息方法
    ApUserFollow selectByFollowId(@Param("burst") String burst, @Param("userId")Long userId, @Param("followId")Integer followId);


    //用户关注信息
    int insert(ApUserFollow record);

    int deleteByFollowId(@Param("burst") String burst,@Param("userId")Long userId,@Param("followId")Integer followId);
}
