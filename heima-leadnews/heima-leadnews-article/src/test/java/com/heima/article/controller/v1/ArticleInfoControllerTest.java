package com.heima.article.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.article.ArticleJarApplication;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = ArticleJarApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc//开启mockkMvc测试
public class ArticleInfoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void testLoadArticleInfo() throws Exception {
        ArticleInfoDto articleInfoDto=new ArticleInfoDto();
        articleInfoDto.setArticleId(10120);
        MockHttpServletRequestBuilder content = MockMvcRequestBuilders.post("/api/v1/article/load_article_info")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(articleInfoDto));
        mockMvc.perform(content).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testLoadArticleBehavior() throws Exception {
        ArticleInfoDto articleInfoDto=new ArticleInfoDto();
        articleInfoDto.setArticleId(10121);
        articleInfoDto.setAuthorId(2104);
//        articleInfoDto.setEquipmentId(1);
        ApUser user=new ApUser();
        user.setId(2l);
        AppThreadLocalUtils.setUser(user);
        MockHttpServletRequestBuilder content = MockMvcRequestBuilders.post("/api/v1/article/load_article_behavior")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(articleInfoDto));
        mockMvc.perform(content).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
