package com.heima.article.service.impl;

import com.heima.article.service.AppArticleService;
import com.heima.common.article.constans.ArticleConstans;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.mappers.app.ApArticleMapper;
import com.heima.model.mappers.app.ApUserArticleListMapper;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserArticleList;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")//不呈现所有的警告
public class AppArticleServiceImpl implements AppArticleService {

    // 单页最大加载的数字
    private final  static short MAX_PAGE_SIZE = 50;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApUserArticleListMapper apUserArticleListMapper;

    //俩个参数，type： 1： 加载更多，2： 加载最新
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        //参数校验
        if (dto==null){
            dto=new ArticleHomeDto();//这样就不会报错
        }
        //时间校验
        if (dto.getMaxBehotTime()==null){
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime()==null){
            dto.setMinBehotTime(new Date());
        }
        //分页大小参数校验
        Integer size = dto.getSize();
        if (size<=0 || size==null){
            size=20;
        }
        dto.setSize(Math.min(size,MAX_PAGE_SIZE));//Math.min(size,MAX_PAGE_SIZE)这个的意思是如果size大于50就取MAX_PAGE_SIZE的值，如果小于MAX_PAGE_SIZE就等于size
        //文章频道参数校验
        if (StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleConstans.DEFAULT_TAG);//查询所有的标签
        }
        //类型参数校验
        if (!(type==ArticleConstans.LOADTYPE_LOAD_MORE) && !(type==ArticleConstans.LOADTYPE_LOAD_NEW)){
            type=ArticleConstans.LOADTYPE_LOAD_MORE;
        }
        //获取用户的信息
        ApUser user = AppThreadLocalUtils.getUser();
        //判断当前用户是否存在
        if (user!=null){
            //存在，加载推荐的文章
            List<ApArticle> userArticle=getUserArticle(user,dto,type);
            return ResponseResult.okResult(userArticle);
        }else {
            //不存在，加载默认的文章
            List<ApArticle> defaultArticle=getDefaultArticle(dto,type);
            return ResponseResult.okResult(defaultArticle);
        }
    }

    //从默认的文章中获取信息
    private List<ApArticle> getDefaultArticle(ArticleHomeDto dto, Short type) {
        return apArticleMapper.loadArticleListByLocation(dto,type);
    }

    //先从用户的推荐表中查找文章信息，如果没有再从默认文章中获取文章信息
    private List<ApArticle> getUserArticle(ApUser user, ArticleHomeDto dto, Short type) {
        List<ApUserArticleList> list = apUserArticleListMapper.loadArticleIdListByUser(user,dto,type);
        if(!list.isEmpty()){
            List<ApArticle> temp = apArticleMapper.loadArticleListByIdList(list);
            return temp;
        }else{
            return getDefaultArticle(dto,type);
        }
    }
}
