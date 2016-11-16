package com.mljr.commodity.mapper;

import com.mljr.commodity.dto.CommodityQuery;
import com.mljr.commodity.dto.Sku;

import java.util.List;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 2016/11/10
 */
public interface ImageMapper {

    Integer countAll() throws Exception;

    List<Sku> queryCommodityList(CommodityQuery query) throws Exception;
}
