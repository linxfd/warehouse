package com.pn.config;

import com.pn.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 原生servlet的配置类
 */
@Configuration
public class ServletConfig {

    //注入redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 注册原生的Servlet的Filter
     */
    @Bean
    public FilterRegistrationBean securityFilter(){
        //创建bean对象
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //创建SecurityFilter对象
        SecurityFilter securityFilter = new SecurityFilter();
        //手动给SecurityFilter对象注入redis模板
        securityFilter.setStringRedisTemplate(stringRedisTemplate);
        //注册SecurityFilter
        filterRegistrationBean.setFilter(securityFilter);
        //配置SecurityFilter对象来拦截所有的请求
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
