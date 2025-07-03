package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Movies;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电影信息表 Mapper 接口
 * </p>
 *
 * @author lxp
 * @since 2025-06-25
 */
@Mapper
public interface MoviesMapper extends BaseMapper<Movies> {
    Page<Movies> pageList(@Param("page")Page<Movies> page, @Param("movies") Movies movies);

    /**
     * 统计给定日期范围内上架的电影总数
     * @param startDate  上映开始日期
     * @param endDate    上映结束日期
     * @return 上架电影总数
     */
    @Select("SELECT COUNT(*) FROM movies WHERE update_time >= #{startDate} AND update_time <= #{endDate} AND is_putaway = 1")
    int countPutawayMovies(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    /**
     * 获取指定日期范围内上架电影的数量
     * @param startDate  上架开始日期
     * @param endDate    上架结束日期
     * @return  在指定日期范围内上架电影的数量
     */
    @Select("SELECT COUNT(*) FROM movies WHERE update_time >= #{startDate} AND update_time <= #{endDate} AND is_putaway = 1")
    int selectCountPutawayMovies(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 查询指定日期范围内每天上架（is_putaway = 1）的电影数量
     * @param startDate 查询的开始日期
     * @param endDate 查询的结束日期
     * @return 包含日期和对应电影数量的Map列表
     */
    @Select("SELECT DATE_FORMAT(update_time, '%Y-%m-%d') AS update_date, " +
            "COUNT(*) AS movie_count FROM movies " +
            "WHERE update_time BETWEEN #{startDate} AND #{endDate} AND is_putaway = 1 " +
            "GROUP BY DATE_FORMAT(update_time, '%Y-%m-%d') " +
            "ORDER BY update_date")
    List<Map<String, Object>> selectMovieCountsByDateRange(@Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);
}
