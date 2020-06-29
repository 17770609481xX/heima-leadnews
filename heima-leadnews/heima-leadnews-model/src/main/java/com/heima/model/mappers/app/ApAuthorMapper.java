package com.heima.model.mappers.app;

import com.heima.model.article.pojos.ApAuthor;

public interface ApAuthorMapper {

    //按照作者ID查询作者
    ApAuthor selectById(Integer id);
}
