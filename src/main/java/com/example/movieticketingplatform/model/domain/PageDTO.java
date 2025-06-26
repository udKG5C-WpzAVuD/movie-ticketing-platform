package com.example.movieticketingplatform.model.domain;

import lombok.Getter;

public class PageDTO {
    private Integer pageNo;
    private Integer pageSize = 10;

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
