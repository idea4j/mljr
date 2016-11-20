package com.mljr.demo.jedis;

import com.mljr.domain.User;
import com.mljr.facade.SpikeFacade;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RedisDemo {

    @Test
    public void run1() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-redis.xml","applicationContext.xml");
        SpikeFacade facade = ac.getBean(SpikeFacade.class);
        User user = facade.querySpikeList(1);
        System.out.println(ToStringBuilder.reflectionToString(user));
    }

}
