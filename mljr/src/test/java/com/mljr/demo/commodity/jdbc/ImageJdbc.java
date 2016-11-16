package com.mljr.demo.commodity.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.mljr.commodity.dto.Sku;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 2016/11/10
 */
public class ImageJdbc {

    private BlockingDeque<List<Sku>> deque = new LinkedBlockingDeque<>();

    public BlockingDeque<List<Sku>> getDeque() {
        return deque;
    }

    private static DataSource dataSource;

    static {
        dataSource = new DruidDataSource();
    }

    private Sku getZouXiuList() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.50.103:3316/installment?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull", "user_fucent", "fucent");
        String sql = "SELECT * FROM s_sku WHERE channel = 3";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            String detail = res.getString("introduction");
            System.out.println(detail);
        }
        return null;
    }


}
