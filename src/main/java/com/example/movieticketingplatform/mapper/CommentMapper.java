package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    Page<Comment> getCommentPage(@Param("page") Page<Comment> page, @Param("com") Comment com);

    int insertComment(@Param("uid") int uid, @Param("content") String content, @Param("category") String category, @Param("date") Date date);

    List<Comment> getUnsolvedComments();

    Page<Comment> getAllCommentPage(@Param("page") Page<Comment> page, @Param("com") Comment com);

    int updateReply(@Param("id") int id, @Param("reply") String reply, @Param("date") Date date);

    int updateStatus(@Param("id") int id, @Param("date") Date date);
}
