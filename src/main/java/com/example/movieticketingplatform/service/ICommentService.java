package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieticketingplatform.model.domain.PageDTO;

import java.util.List;


public interface ICommentService extends IService<Comment> {

    Page<Comment> getCommentPage(Comment com, PageDTO pageDTO);

    boolean addComment(int uid, String content, String category);

    List<Comment> getUnsolvedComments();

    Page<Comment> getAllComments(Comment com, PageDTO pageDTO);

    boolean updateReply(int id, String reply);

    boolean updateStatus(int id);
}
