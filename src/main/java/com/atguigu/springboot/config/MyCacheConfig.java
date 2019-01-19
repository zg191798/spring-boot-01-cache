package com.atguigu.springboot.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zhangge
 * @date 2019/1/19 - 14:21
 */
@Configuration
public class MyCacheConfig {

    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator(){
        return new KeyGenerator(){
            public Object generate(Object target, Method method, Object...params){
                return method.getName()+"["+ Arrays.asList(params)+"]";
            }
        };
    }
}
