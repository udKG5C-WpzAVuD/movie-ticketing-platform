package com.example.movieticketingplatform.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.movieticketingplatform.mapper.MoviesMapper;
import com.example.movieticketingplatform.mapper.OrdersMapper;
import com.example.movieticketingplatform.mapper.UserMapper;
import com.example.movieticketingplatform.model.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private UserMapper usersMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private MoviesMapper moviesMapper;
    @Autowired
    private PdfReportGenerator pdfReportGenerator;

    public ReportData generateReportData(String reportType, LocalDate startDate, LocalDate endDate) {
        ReportData reportData = new ReportData();

        // 新注册用户
        int newUserCount = usersMapper.selectCount(new QueryWrapper<User>()
                .ge("registration_time", startDate)
                .le("registration_time", endDate)).intValue();
        reportData.setNewUserCount(newUserCount);

        // 订单总量
        int orderCount = ordersMapper.selectCount(new QueryWrapper<Orders>()
                .ge("create_time", startDate)
                .le("create_time", endDate)).intValue();
        reportData.setOrderCount(orderCount);

        // 总票房 - 注意这里要除以100，
        Double totalRevenue = ordersMapper.selectTotalAmount(startDate, endDate);
        reportData.setTotalRevenue(totalRevenue != null ? totalRevenue / 100 : 0.0);

        // 上架电影总数
        int movieCount = moviesMapper.selectCount(new QueryWrapper<Movies>()
                .ge("update_time", startDate)
                .le("update_time", endDate)
                .eq("is_putaway", 1)).intValue();
        reportData.setMovieCount(movieCount);

        return reportData;
    }

    public JSONObject generateChartData(String reportType, LocalDate startDate, LocalDate endDate) {
        // 构建X轴的日期
        List<String> xAxisData = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            xAxisData.add(current.toString());
            current = current.plusDays(1);
        }
        //设置y轴，自动计算最大值与间隔
        JSONObject yAxis = new JSONObject();
        yAxis.put("type", "value");
        yAxis.put("min", 0);
        JSONObject option = new JSONObject();
        option.put("yAxis", yAxis);
        // 2. 查询并填充各数据源
        List<Map<String, Object>> filledUserData = fillMissingDates(
                usersMapper.selectUserCountsByDateRange(startDate, endDate),
                xAxisData, "registration_date", "user_count");

        List<Map<String, Object>> filledOrderData = fillMissingDates(
                ordersMapper.selectOrderCountsByDateRange(startDate, endDate),
                xAxisData, "order_date", "order_count");

        List<Map<String, Object>> filledRevenueData = fillMissingDates(
                ordersMapper.selectTotalRevenueByDateRange(startDate, endDate),
                xAxisData, "order_date", "total_revenue");

        List<Map<String, Object>> filledMovieData = fillMissingDates(
                moviesMapper.selectMovieCountsByDateRange(startDate, endDate),
                xAxisData, "update_date", "movie_count");

        // 3. 提取数值列表
        List<Integer> newUserCountData = filledUserData.stream()
                .map(map -> (Integer) map.get("user_count"))
                .collect(Collectors.toList());

        List<Integer> orderCountData = filledOrderData.stream()
                .map(map -> (Integer) map.get("order_count"))
                .collect(Collectors.toList());

        List<Double> revenueDataList = filledRevenueData.stream()
                .map(map -> ((Number) map.get("total_revenue")).doubleValue()/100)
                .collect(Collectors.toList());

        List<Integer> movieCountData = filledMovieData.stream()
                .map(map -> (Integer) map.get("movie_count"))
                .collect(Collectors.toList());

        // 构建ECharts配置项

        option.put("title", new JSONObject().fluentPut("text", "核心数据折线图"));
        option.put("tooltip", new JSONObject().fluentPut("trigger", "axis"));
        option.put("legend", new JSONObject().fluentPut("data", new String[]{"新注册用户（个）", "订单总量（个）", "总票房（百元）","上架电影总数（个）"}));

        JSONObject grid = new JSONObject();
        grid.put("left", "3%");
        grid.put("right", "4%");
        grid.put("bottom", "3%");
        grid.put("containLabel", true);
        option.put("grid", grid);

        JSONObject toolbox = new JSONObject();
        JSONObject feature = new JSONObject();
        feature.put("saveAsImage", new JSONObject());
        toolbox.put("feature", feature);
        option.put("toolbox", toolbox);

        option.put("xAxis", new JSONObject()
                .fluentPut("type", "category")
                .fluentPut("boundaryGap", false)
                .fluentPut("data", xAxisData));

        option.put("yAxis", new JSONObject().fluentPut("type", "value"));

        List<JSONObject> seriesList = new ArrayList<>();

        // 新注册用户
        JSONObject newUserSeries = new JSONObject();
        newUserSeries.put("name", "新注册用户（个）");
        newUserSeries.put("type", "line");
        //newUserSeries.put("stack", "Total");
        newUserSeries.put("data", newUserCountData);
        seriesList.add(newUserSeries);

        // 订单总量
        JSONObject orderSeries = new JSONObject();
        orderSeries.put("name", "订单总量（个）");
        orderSeries.put("type", "line");
        //orderSeries.put("stack", "Total");
        orderSeries.put("data", orderCountData);
        seriesList.add(orderSeries);

        // 总票房
        JSONObject revenueSeries = new JSONObject();
        revenueSeries.put("name", "总票房（百元）");
        revenueSeries.put("type", "line");
        //revenueSeries.put("stack", "Total");
        revenueSeries.put("data", revenueDataList);
        seriesList.add(revenueSeries);

        // 上架电影总数
        JSONObject movieSeries = new JSONObject();
        movieSeries.put("name", "上架电影总数（个）");
        movieSeries.put("type", "line");
        //movieSeries.put("stack", "Total");
        movieSeries.put("data", movieCountData);
        seriesList.add(movieSeries);

        option.put("series", seriesList);

        return option;
    }


    public byte[] generatePdfReport(String reportType, LocalDate startDate, LocalDate endDate, String chartImage) throws IOException {
        ReportData reportData = generateReportData(reportType, startDate, endDate);
        JSONObject chartData = generateChartData(reportType, startDate, endDate);

        return pdfReportGenerator.generatePdfByteArray(reportType, startDate, endDate, reportData, chartData, chartImage);
    }

    // 辅助方法：填充缺失日期的数据为零
    private List<Map<String, Object>> fillMissingDates(List<Map<String, Object>> rawData,
                                                       List<String> dateSeries,
                                                       String dateKey,
                                                       String valueKey) {
        Map<String, Integer> dateMap = rawData.stream()
                .collect(Collectors.toMap(
                        map -> map.get(dateKey).toString(),
                        map -> ((Number) map.get(valueKey)).intValue()
                ));

        return dateSeries.stream()
                .map(date -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put(dateKey, date);
                    entry.put(valueKey, dateMap.getOrDefault(date, 0));
                    return entry;
                })
                .collect(Collectors.toList());
    }
}
