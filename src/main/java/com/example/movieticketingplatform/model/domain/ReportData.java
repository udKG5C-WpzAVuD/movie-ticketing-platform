package com.example.movieticketingplatform.model.domain;

import lombok.Data;

@Data
public class ReportData {
    private int newUserCount;
    private int orderCount;
    private double totalRevenue;
    private int movieCount;

    public int getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(int newUserCount) {
        this.newUserCount = newUserCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }
}
