package com.heima.model.mappers.app;

import com.heima.model.article.pojos.ApArticleContent;

public interface ApArticleContentMapper {
    //通过文章id查找文章
    ApArticleContent selectByArticleId(Integer articleId);
}
