package com.heima.article.service;

import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;

public interface AppArticleService {

    //俩个参数，type： 1： 加载更多，2： 加载最新
    ResponseResult load(ArticleHomeDto dto, Short type);
}
