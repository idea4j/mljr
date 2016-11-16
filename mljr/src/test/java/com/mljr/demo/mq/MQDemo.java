package com.mljr.demo.mq;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 2016/11/12
 */
public class MQDemo {

    /**
     * 消息生产者
     *
     * @throws Exception
     */
    @Test
    public void run1() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq-producer.xml");
        JmsTemplate template = ac.getBean(JmsTemplate.class);
        ActiveMQQueue queue = ac.getBean(ActiveMQQueue.class);
        template.send(queue, session -> session.createTextMessage("2"));
    }


    /**
     * 消息消费者
     *
     * @throws Exception
     */
    @Test
    public void run2() throws Exception {
        ReentrantLock lock = new ReentrantLock();
        if (lock.tryLock()) {
            try {
                System.out.println("尝试成功获取到了锁！");
            } finally {
                lock.unlock();
            }
        }
    }

}
