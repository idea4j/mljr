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
        list.add(2);
        list.add(3);
        list.add(4);
        long start = System.currentTimeMillis();
        list.stream().parallel().forEach(System.out::println);
        long end = System.currentTimeMillis();
        System.out.println("=====================");
        System.out.println(end - start);
        start = System.currentTimeMillis();
        list.stream().forEach(System.out::println);
        end = System.currentTimeMillis();
        System.out.println("=====================");
        System.out.println(end - start);

    }

    private Integer haha;
    @Test
    public void run2() throws Exception {
        Logger logger = LoggerFactory.getLogger(LambdaDemo.class);
        logger.info("haha {}", "123");
    }

}
