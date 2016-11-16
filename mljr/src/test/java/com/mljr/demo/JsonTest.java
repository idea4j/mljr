package com.mljr.demo;

import com.mljr.pojo.Province;
import com.mljr.utils.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonTest {
    @Test
    public void run1() throws Exception {
        String json = "[{\"k\":\"1\",\"v\":\"辽宁\"},{\"k\":\"2\",\"v\":\"湖南\"},{\"k\":\"3\",\"v\":\"内蒙古\"}]";
        List<Province> list = new ArrayList<>();
        Province p1 = new Province("1", "辽宁");
        Province p2 = new Province("2", "湖南");
        Province p3 = new Province("3", "内蒙古");
        list.add(p1);
        list.add(p2);
        list.add(p3);
        String s = JsonUtils.objectToJson(list);
        System.out.println(s);

        List<Province> provinces = JsonUtils.jsonToList(json, Province.class);
        for (Province p : provinces) {
            System.out.println(p.getK());
        }
    }

}
