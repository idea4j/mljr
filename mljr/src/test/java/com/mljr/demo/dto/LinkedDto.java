package com.mljr.demo.dto;

import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;

import java.io.FileReader;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 2016/11/9
 */
public class LinkedDto {

    @Test
    public void run1() throws Exception {
        FileReader fr = new FileReader("/Users/Tinker/Workspaces/git/mljr/src/test/resources/index.html");
        int ch = 0;
        StringBuilder sbr = new StringBuilder();
        while ((ch = fr.read()) != -1) {
            sbr.append((char) ch);
        }
        fr.close();
        // String regex = "<img.*src=(.*?)[^>]*?>";
        String regex = "<img\\s+[^>]*\\s*src\\s*=\\s*\"([^>]*?)\"[^>]*?>";
        String tagHead = "<img src=\"";
        String tagTail = "\" alt=\"\" />";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(sbr.toString());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String temp = matcher.group(1);
            matcher.appendReplacement(sb, tagHead + "http://www.baidu.com" + tagTail);
        }
        matcher.appendTail(sb);
        System.out.println(sb.toString());
    }

    @Test
    public void run2() throws Exception {
        String regex = "<img\\s+[^>]*\\s*src\\s*=\\s*\"([^>]*?)\"[^>]*?>";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher("<img src=\"http://123123123.wwet123.123.com\">");
        while (matcher.find()) {
            String tmp1 = matcher.group(1);
            System.out.println("group1:" + tmp1);
        }
    }


    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        JmsTemplate template;
    }
}

