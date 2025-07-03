package com.example.movieticketingplatform.web.controller;


import com.example.movieticketingplatform.common.utls.ExportUtil;
import com.example.movieticketingplatform.model.domain.Movies;
import com.example.movieticketingplatform.model.domain.Orders;
import com.example.movieticketingplatform.model.domain.User;
import com.example.movieticketingplatform.service.IMoviesService;
import com.example.movieticketingplatform.service.IOrderService;
import com.example.movieticketingplatform.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // 允许来自任何来源的请求
public class ExportController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IMoviesService movieService;

    /**
     * 导出数据接口
     * @param exportType 导出数据类型 (user, order, movie)
     * @param exportFormat 导出格式 (excel, csv)
     * @param response HttpServletResponse 用于设置响应头和写入文件流
     * @return ResponseEntity<byte[]> 包含文件字节流
     * @throws IOException
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(
            @RequestParam("exportType") String exportType,
            @RequestParam("exportFormat") String exportFormat,
            HttpServletResponse response) throws IOException {

        String filename = "";
        byte[] fileBytes = null;
        String contentType = "";
        Map<String, String> headers = new LinkedHashMap<>(); // 保持插入顺序

        switch (exportType) {
            case "user":
                List<User> users = userService.listAllUsers();
                if (users.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                filename = "用户信息";
                headers.put("id", "ID");
                headers.put("username", "用户名");
                headers.put("email", "邮箱");
                headers.put("password", "密码");
                headers.put("registrationTime", "注册时间");
                headers.put("status", "状态");
                headers.put("role", "角色");
                headers.put("contactPhone", "联系电话");
                if ("excel".equalsIgnoreCase(exportFormat)) {
                    fileBytes = ExportUtil.exportExcel(users, headers);
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    filename += ".xlsx";
                } else if ("csv".equalsIgnoreCase(exportFormat)) {
                    fileBytes = ExportUtil.exportCsv(users, headers);
                    contentType = "text/csv";
                    filename += ".csv";
                }
                break;
            case "order":
                List<Orders> orders = orderService.listAllOrders();
                if (orders.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                filename = "订单详情";
                headers.put("id", "订单ID");
                headers.put("orderNo", "订单号");
                headers.put("userId", "用户ID");
                headers.put("sessionId", "场次ID");
                headers.put("totalAmount", "总金额");
                headers.put("contactPhone", "联系电话");
                headers.put("paymentMethod", "支付方式");
                headers.put("paymentTime", "支付时间");
                headers.put("paymentTransactionId", "支付交易号");
                headers.put("createTime", "创建时间");
                headers.put("updateTime", "更新时间");
                headers.put("code", "座位号");
                if ("excel".equalsIgnoreCase(exportFormat)) {
                    fileBytes = ExportUtil.exportExcel(orders, headers);
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    filename += ".xlsx";
                } else if ("csv".equalsIgnoreCase(exportFormat)) {
                    fileBytes = ExportUtil.exportCsv(orders, headers);
                    contentType = "text/csv";
                    filename += ".csv";
                }
                break;
            case "movie":
                List<Movies> movies = movieService.listAllMovies();
                if (movies.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                filename = "电影信息";
                headers.put("id", "电影ID");
                headers.put("title", "电影名");
                headers.put("director", "导演");
                headers.put("actors", "主演");
                headers.put("genre", "类型");
                headers.put("duration", "时长（分钟）");
                headers.put("releaseDate", "上映日期");
                headers.put("language", "语言");
                headers.put("description", "描述");
                headers.put("isPutaway", "是否上架");
                headers.put("contactPhone", "联系电话");
                headers.put("count", "收藏");
                headers.put("createTime", "创建时间");
                headers.put("updateTime", "创建时间");
                headers.put("price", "价格");
                if ("excel".equalsIgnoreCase(exportFormat)) {
                    fileBytes = ExportUtil.exportExcel(movies, headers);
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    filename += ".xlsx";
                } else if ("csv".equalsIgnoreCase(exportFormat)) {
                    fileBytes = ExportUtil.exportCsv(movies, headers);
                    contentType = "text/csv";
                    filename += ".csv";
                }
                break;
            default:
                return ResponseEntity.badRequest().body("Unsupported export type or format.".getBytes(StandardCharsets.UTF_8));
        }

        if (fileBytes == null) {
            return ResponseEntity.internalServerError().body("File generation failed.".getBytes(StandardCharsets.UTF_8));
        }

        // 设置响应头
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()))
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileBytes);
    }
}
