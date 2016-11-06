package com.mljr.demo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 16/11/4
 */
public class LambdaDemo {

    @Test
    public void run1() throws Exception {
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.forEach((haha) -> System.out.println(haha+"1"));
        list.forEach(System.out::println);
    }

    @Test
    public void run2() throws Exception {
        Logger logger = LoggerFactory.getLogger(LambdaDemo.class);
        logger.info("日志记录：{} -->> {}", "param1", "param2");
    }
}
