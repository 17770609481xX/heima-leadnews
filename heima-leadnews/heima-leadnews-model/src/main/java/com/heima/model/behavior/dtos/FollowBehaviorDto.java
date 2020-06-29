package com.heima.model.behavior.dtos;

import com.heima.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class FollowBehaviorDto {
    // 设备ID
    @IdEncrypt
    Integer equipmentId;
    // 文章ID
    @IdEncrypt
    Integer articleId;
    //关注用户id
    @IdEncrypt
    Integer followId;
}
