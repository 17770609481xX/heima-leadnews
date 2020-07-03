package com.heima.article.service.impl;

import com.heima.article.service.AppArticleInfoService;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.article.pojos.ApCollection;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApLikesBehavior;
import com.heima.model.behavior.pojos.ApUnlikesBehavior;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.crawler.core.parse.ZipUtils;
import com.heima.model.mappers.app.*;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserFollow;
import com.heima.utils.common.BurstUtils;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
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
    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ApCollectionMapper apCollectionMapper;
    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;
    @Autowired
    private ApUnlikesBehaviorMapper apUnlikesBehaviorMapper;
    @Autowired
    private ApUserFollowMapper apUserFollowMapper;

    @Override
    public ResponseResult getArticleInfo(Integer articleId) {
        System.out.println(articleId);
        if (articleId==null || articleId<=1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Map<String,Object> data=new HashMap<>();
        //根据文章id查找config信息
        ApArticleConfig apArticleConfig = apArticleConfigMapper.selectByArticleId(articleId);
        System.out.println(apArticleConfig);
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

    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {
        //查找用户的登录信息
        ApUser user = AppThreadLocalUtils.getUser();
        //用户名和设备不能同时为空
        if (user==null && dto.getEquipmentId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //通过用户id或者设备id查询行为实体
        Long userId = null;
        if(user!=null){
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipemntId(userId, dto.getEquipmentId());
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误
        if(apBehaviorEntry==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        boolean isUnLike=false,isLike=false,isCollection=false,isFollow=false;
        //查询分片id
        String burst= BurstUtils.groudOne(apBehaviorEntry.getId());

        //判断是否已经收藏了
        ApCollection apCollection = apCollectionMapper.selectForEntryId(burst, apBehaviorEntry.getId(), dto.getArticleId(),ApCollection.Type.ARTICLE.getCode());
        if (apCollection!=null){
            isCollection=true;
        }
        //查看是否点赞
        ApLikesBehavior apLikesBehavior = apLikesBehaviorMapper.selectLastLike(burst, apBehaviorEntry.getId(), dto.getArticleId(), ApCollection.Type.ARTICLE.getCode());
        if (apLikesBehavior!=null && apLikesBehavior.getOperation()==ApLikesBehavior.Operation.LIKE.getCode()){
            isLike=true;
        }
        //查看不喜欢表
        ApUnlikesBehavior apUnlikesBehavior = apUnlikesBehaviorMapper.selectLastUnLike(apBehaviorEntry.getId(), dto.getArticleId());
        if (apUnlikesBehavior!=null && apUnlikesBehavior.getType()== ApUnlikesBehavior.Type.UNLIKE.getCode()){
            isUnLike=true;
        }
        //查询作者信息
        ApAuthor apAuthor = apAuthorMapper.selectById(dto.getAuthorId());
        //查看关注表，看看阅读者是否关注了作者
        if (apAuthor!=null && userId!=null && apAuthor.getUserId()!=null){
            ApUserFollow apUserFollow = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(userId), userId ,apAuthor.getUserId().intValue());
            if (apUserFollow!=null){
                isFollow=true;
            }
        }
        Map<String,Object> data = new HashMap<>();
        data.put("isfollow",isFollow);
        data.put("islike",isLike);
        data.put("isunlike",isUnLike);
        data.put("iscollection",isCollection);

        return ResponseResult.okResult(data);
    }
}
