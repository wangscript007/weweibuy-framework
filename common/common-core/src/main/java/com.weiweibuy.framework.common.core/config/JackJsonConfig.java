package com.weiweibuy.framework.common.core.config;

import com.weiweibuy.framework.common.core.utils.JackJsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/3/1 10:57
 **/
@Configuration
public class JackJsonConfig {


    @Bean
    public JackJsonUtils jackJsonUtils(){
        return new JackJsonUtils();
    }

}
