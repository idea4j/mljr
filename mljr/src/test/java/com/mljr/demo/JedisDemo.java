package com.mljr.demo;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 16/11/4
 */
public class JedisDemo {

    @Test
    public void run1() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
        RedisTemplate tmp = ac.getBean(RedisTemplate.class);
        ValueOperations value = tmp.opsForValue();
        value.set("haha", "haha");
        BoundValueOperations bound = tmp.boundValueOps("haha");
        bound.expire(10000L, TimeUnit.MILLISECONDS);
        System.out.println("hehe");
    }
}
