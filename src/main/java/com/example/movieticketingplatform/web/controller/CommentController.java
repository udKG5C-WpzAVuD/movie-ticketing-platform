package com.example.movieticketingplatform.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.ICommentService;
import com.example.movieticketingplatform.model.domain.Comment;

import java.util.List;


@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final Logger logger = LoggerFactory.getLogger( CommentController.class );

    @Autowired
    private ICommentService commentService;

    @GetMapping("getComments")
    public JsonResponse getComments(Comment com, PageDTO pageDTO)throws Exception{
        Page<Comment> commentPage = commentService.getCommentPage(com, pageDTO);
        return JsonResponse.success(commentPage);
    }

    @Data
    public static class AddCommentRequest {
        private int uid;
        private String content;
        private String category;

    }
    @PostMapping("addComment")
    public JsonResponse addComment(@RequestBody CommentController.AddCommentRequest request) throws Exception {
        boolean success = commentService.addComment(request.getUid(), request.getContent(), request.getCategory());
        return success ? JsonResponse.success("插入成功") : JsonResponse.failure("插入失败");
    }

    @GetMapping("getUnsolvedComments")
    public JsonResponse getUnsolvedComments() throws Exception{
        List<Comment> comments = commentService.getUnsolvedComments();
        return JsonResponse.success(comments);
    }

    @GetMapping("getAllComments")
    public JsonResponse getAllComments(Comment com, PageDTO pageDTO) throws Exception{
        Page<Comment> commentPage = commentService.getAllComments(com, pageDTO);
        return JsonResponse.success(commentPage);
    }

    @Data
    public static class UpdateReplyRequest {
        private int id;
        private String reply;
    }
    @PostMapping("updateReply")
    public JsonResponse updateReply(@RequestBody CommentController.UpdateReplyRequest request) throws Exception {
        boolean success = commentService.updateReply(request.getId(), request.getReply());
        return success ? JsonResponse.success("更新成功") : JsonResponse.failure("更新失败");
    }

    @Data
    public static class UpdateStatusRequest {
        private int id;
    }
    @PostMapping("updateStatus")
    public JsonResponse updateStatus(@RequestBody CommentController.UpdateStatusRequest request) throws Exception {
        System.out.println(request.getId());
        boolean success = commentService.updateStatus(request.getId());
        return success ? JsonResponse.success("更新成功") : JsonResponse.failure("更新失败");
    }

}

