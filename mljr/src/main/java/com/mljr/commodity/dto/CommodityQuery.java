package com.mljr.commodity.dto;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 2016/11/10
 */
public class CommodityQuery {

    private Integer page;
    private Integer pageSize;

    public Integer getStart() {
        return pageSize * (page - 1);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public CommodityQuery() {
    }

    public CommodityQuery(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
}
