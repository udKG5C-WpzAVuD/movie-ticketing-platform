package com.example.movieticketingplatform.model.dto;



public class PageDTO {
    private Integer pageNum = 1;  // 默认值
    private Integer pageSize = 10; // 默认值

    public PageDTO() {
        pageNum = 1;  // 默认值
       pageSize = 10;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
