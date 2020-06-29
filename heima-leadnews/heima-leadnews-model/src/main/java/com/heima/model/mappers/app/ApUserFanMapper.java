package com.heima.model.mappers.app;

import com.heima.model.user.pojos.ApUserFan;
import org.apache.ibatis.annotations.Param;

public interface ApUserFanMapper {

    //添加APP用户粉丝信息
    int insert(ApUserFan record);

    ApUserFan selectByFansId(@Param("burst") String burst,@Param("userId") Integer userId , @Param("fansId") Long fansId);

    int deleteByFansId(@Param("burst")String burst,@Param("userId")Integer userId ,@Param("fansId")Long fansId);
}
