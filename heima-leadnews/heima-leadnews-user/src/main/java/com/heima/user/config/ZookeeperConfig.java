package com.heima.user.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*加载zookeeper*/
@Configuration
@ComponentScan("com.heima.common.zookeeper")
public class ZookeeperConfig {

}
