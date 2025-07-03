package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Comment;
import com.example.movieticketingplatform.mapper.CommentMapper;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.example.movieticketingplatform.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public Page<Comment> getCommentPage(Comment com, PageDTO pageDTO) {
        Page<Comment> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        page = commentMapper.getCommentPage(page, com);
        return page;
    }

    @Override
    public boolean addComment(int uid, String content, String category) {
        Date date = new Date();
        return commentMapper.insertComment(uid, content, category, date) > 0;
    }

    @Override
    public List<Comment> getUnsolvedComments() {
        List<Comment> comments = new ArrayList<>();
        comments = commentMapper.getUnsolvedComments();
        return comments;
    }

    @Override
    public Page<Comment> getAllComments(Comment com, PageDTO pageDTO) {
        Page<Comment> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        page = commentMapper.getAllCommentPage(page, com);
        return page;
    }

    @Override
    public boolean updateReply(int id, String reply) {
        Date date = new Date();
        return commentMapper.updateReply(id, reply, date) > 0;
    }

    @Override
    public boolean updateStatus(int id) {
        Date date = new Date();
        return commentMapper.updateStatus(id, date) > 0;
    }
}
