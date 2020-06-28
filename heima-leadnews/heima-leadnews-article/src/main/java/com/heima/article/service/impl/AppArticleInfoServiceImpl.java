package com.heima.article.service.impl;

import com.heima.article.service.AppArticleInfoService;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.crawler.core.parse.ZipUtils;
import com.heima.model.mappers.app.ApArticleConfigMapper;
import com.heima.model.mappers.app.ApArticleContentMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class AppArticleInfoServiceImpl implements AppArticleInfoService {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Override
    public ResponseResult getArticleInfo(Integer articleId) {
        if (articleId==null || articleId<=1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Map<String,Object> data=new HashMap<>();
        //根据文章id查找config信息
        ApArticleConfig apArticleConfig = apArticleConfigMapper.selectByArticleId(articleId);
        //查看文章是否删除
        if (apArticleConfig==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }else if (!apArticleConfig.getIsDelete()){
            ApArticleContent apArticleContent = apArticleContentMapper.selectByArticleId(articleId);
            //对内容进行解压
            String content = ZipUtils.gunzip(apArticleContent.getContent());
            apArticleContent.setContent(content);
            data.put("content",apArticleContent);
        }
        data.put("config",apArticleConfig);
        return ResponseResult.okResult(data);
    }
}
