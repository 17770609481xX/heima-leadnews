package com.heima.model.mappers.app;

import com.heima.model.behavior.pojos.ApReadBehavior;
import org.apache.ibatis.annotations.Param;

//对于用户的阅读行为的操作
public interface ApReadBehaviorMapper {

    int insert(ApReadBehavior record);
    int update(ApReadBehavior record);
    ApReadBehavior selectByEntryId(@Param("burst") String burst,@Param("entryId") Integer entryId,@Param("articleId") Integer articleId);
}
