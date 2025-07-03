package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User> {
    Page<User> pageList(@Param("page") Page<User> page, @Param("user") User user);

    /**
     *  统计给定日期范围内的新注册用户数量
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return  新注册用户数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE registration_time >= #{startDate} AND registration_time <= #{endDate}")
    int countNewUsers(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 查询给定日期范围内每天的新注册用户数
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return  每天的新注册用户数，以日期和数量的映射形式返回
     */
    @Select("SELECT DATE_FORMAT(registration_time, '%Y-%m-%d') AS registration_date, " +
            "COUNT(*) AS user_count FROM users " +
            "WHERE registration_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE_FORMAT(registration_time, '%Y-%m-%d') " +
            "ORDER BY registration_date")
    List<Map<String, Object>> selectUserCountsByDateRange(@Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);
}
