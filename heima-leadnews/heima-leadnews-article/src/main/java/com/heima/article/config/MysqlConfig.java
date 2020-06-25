package com.heima.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//查询数据库

@Configuration
@ComponentScan("com.heima.common.mysql.core")
public class MysqlConfig {
}
