package com.heima.model.mappers.app;

import com.heima.model.behavior.pojos.ApLikesBehavior;
import org.apache.ibatis.annotations.Param;

public interface ApLikesBehaviorMapper {

    /**
     * 义按照行为实体、点赞内容、点赞操作方式查询点赞信息
     * @return
     */
    ApLikesBehavior selectLastLike(@Param("burst") String burst, @Param("objectId") Integer objectId,@Param("entryId") Integer entryId, @Param("type")Short type);

    //喜欢点赞
    int insert(ApLikesBehavior record);
}
