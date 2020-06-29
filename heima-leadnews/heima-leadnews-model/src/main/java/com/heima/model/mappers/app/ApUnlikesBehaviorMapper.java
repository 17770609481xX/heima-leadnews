package com.heima.model.mappers.app;

import com.heima.model.behavior.pojos.ApUnlikesBehavior;
import org.apache.ibatis.annotations.Param;

public interface ApUnlikesBehaviorMapper {

    /**
     * 定义按照行为实体ID、文章ID查询不喜欢最有一条信息的方法：
     * @return
     */
    ApUnlikesBehavior selectLastUnLike(@Param("entryId") Integer entryId,@Param("articleId") Integer articleId);

    //不喜欢接口
    int insert(ApUnlikesBehavior record);
}
