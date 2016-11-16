package com.mljr.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 2016/11/12
 */
public class MyMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("=======message coming=======");
        if (message instanceof TextMessage) {
            try {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("=========================");
                System.out.println(text);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
