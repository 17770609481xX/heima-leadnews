package com.heima.model.mappers.app;

import com.heima.model.article.pojos.ApArticleConfig;

public interface ApArticleConfigMapper {
    //通过文章id查找文章的状态信息
    ApArticleConfig selectByArticleId(Integer articleId);
}
