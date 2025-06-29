package com.example.movieticketingplatform.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.example.movieticketingplatform.service.OperationLogAUService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IUserService;
import com.example.movieticketingplatform.model.domain.User;



@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private IUserService userService;

    @Autowired
    private OperationLogAUService logService;

    private static final Long DEFAULT_ADMIN_ID = 1L; // 常量定义默认管理员ID

    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<User> getById(@PathVariable("id") Long id)throws Exception {
        User user = userService.getById(id);
        return JsonResponse.success(user);
    }

    //用户显示与分页查询接口
    @GetMapping("pageList")
    public JsonResponse pageList(User user, PageDTO pageDTO)throws Exception{
        Page<User> page = userService.pageList(user, pageDTO);
        return JsonResponse.success(page);
    }

    /**
     * 用户信息更新接口
     * @param user 用户信息对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public JsonResponse<?> updateUser(@RequestBody User user) {
        try {
            // 参数校验
            if (user == null || user.getId() == null) {
                return JsonResponse.failure("参数错误：用户ID不能为空")
                        .setCode(400); // 新增code字段
            }

            // 检查用户是否存在
            User existingUser = userService.getById(user.getId());
            if (existingUser == null) {
                return JsonResponse.failure("用户不存在")
                        .setCode(404);
            }

            //校验合法电话号码
            if (user.getContactPhone() != null && !user.getContactPhone().equals(existingUser.getContactPhone())) {
                // 正则表达式匹配中国大陆手机号(11位)或带区号的固定电话(如010-12345678)
                String phoneRegex = "^(1[3-9]\\d{9})$|^(0\\d{2,3}-?\\d{7,8})$";
                if (!user.getContactPhone().matches(phoneRegex)) {
                    return JsonResponse.failure("电话号码格式不正确")
                            .setCode(400);
                }
            }

            // 校验用户名
            if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
                if (userService.existsByUsername(user.getUsername())) {
                    return JsonResponse.failure("用户名已存在")
                            .setCode(409);
                }
            }

            // 校验邮箱
            if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
                if (userService.existsByEmail(user.getEmail())) {
                    return JsonResponse.failure("邮箱已被注册")
                            .setCode(409);
                }
            }

            // 执行更新
            // 调用Service层的更新方法（包含详细日志记录）
            boolean success = userService.updateUser(user);
            if (success) {
                return JsonResponse.successMessage("更新成功").setCode(0);
            } else {
                return JsonResponse.failure("更新失败").setCode(500);
            }
        } catch (Exception e) {
            return JsonResponse.failure("系统异常: " + e.getMessage())
                    .setCode(500);
        }
    }

}

