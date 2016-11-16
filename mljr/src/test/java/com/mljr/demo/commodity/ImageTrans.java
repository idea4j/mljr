package com.mljr.demo.commodity;

import com.mljr.commodity.dto.CommodityQuery;
import com.mljr.commodity.dto.Sku;
import com.mljr.commodity.mapper.ImageMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ObjectUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 2016/11/10
 */
public class ImageTrans {
    static class ImageResult {
        private Integer id;
        private Integer status;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-datasource.xml");
        ImageMapper mapper = ac.getBean(ImageMapper.class);
        // 查询出所有的商品数量
        // Integer total = mapper.countAll();
        // 分段统计商品
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 接收统计结果
        List<ImageResult> list = new ArrayList<>(200);
        // 设置开始结束
        for (int i = 1; i < 200; i++) {
            int finalI = i;
            Callable<List<ImageResult>> callable = new Callable<List<ImageResult>>() {
                @Override
                public List<ImageResult> call() throws Exception {
                    List<ImageResult> list = new ArrayList<>();
                    // PageHelper.startPage(finalI, 1000);
                    CommodityQuery query = new CommodityQuery(finalI, 1000);
                    List<Sku> skus = mapper.queryCommodityList(query);
                    for (Sku sku : skus) {
                        // 执行替换操作
                        ImageResult result = replaceImageUrl(sku);
                        if (!ObjectUtils.isEmpty(result)) {
                            list.add(result);
                        }
                    }
                    return list;
                }
            };
            Future<List<ImageResult>> future = executorService.submit(callable);
            List<ImageResult> resultList = future.get();
            for (ImageResult result : resultList) {
                if (!ObjectUtils.isEmpty(result)) {
                    list.add(result);
                }
            }
        }
        System.out.println("=================");
        System.out.println(list.size());
        FileWriter fw = new FileWriter("/Users/Tinker/test/ids.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("文件开始:");
        bw.newLine();
        for (ImageResult result : list) {
            System.out.println(result.getId());
            bw.write(result.getId().toString());
            bw.newLine();
        }
        bw.close();
        fw.close();
        executorService.shutdown();
    }

    private static ImageResult replaceImageUrl(Sku sku) {
        String regex = "<img\\s+[^>]*\\s*src\\s*=\\s*\"([^>]*?)\"[^>]*?>";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(sku.getIntroduction());
        int count = 0;
        while (matcher.find()) {
            // 如果找到匹配的，就加一
            if (count > 5) {
                break;
            }
            count++;
        }
        if (count < 1) {
            ImageResult result = new ImageResult();
            result.setId(sku.getId());
            result.setStatus(0);
            return result;
        }
        return null;
    }

}
