package com.example.movieticketingplatform.web.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.ILikesService;
import com.example.movieticketingplatform.model.domain.Likes;


@RestController
@RequestMapping("/api/likes")
public class LikesController {

    private final Logger logger = LoggerFactory.getLogger( LikesController.class );

    @Autowired
    private ILikesService likesService;

    @Data
    public static class AddLikeRequest {
        private int uid;
        private int mid;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }
    }
    @PostMapping("addLike")
    public JsonResponse updateLikes(@RequestBody LikesController.AddLikeRequest request) {
        boolean success = likesService.addLike(request.getUid(), request.getMid());
        return success ? JsonResponse.success("插入成功") : JsonResponse.failure("插入失败");
    }

    @DeleteMapping("deleteLike")
    public JsonResponse deleteLike(
            @RequestParam("uid") int uid,
            @RequestParam("mid") int mid) {
        boolean success = likesService.deleteLike(uid, mid);
        return success ? JsonResponse.success("删除成功") : JsonResponse.failure("删除失败");
    }

    @GetMapping("getLike")
    public JsonResponse getLike(
            @RequestParam("uid") int uid,
            @RequestParam("mid") int mid) {
        boolean success = likesService.getLike(uid, mid);
        return success ? JsonResponse.success("查找记录成功") : JsonResponse.failure("查找记录失败");
    }

}
