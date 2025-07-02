package com.example.movieticketingplatform.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.common.utls.SessionUtils;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.example.movieticketingplatform.service.OperationLogAUService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import com.example.movieticketingplatform.model.domain.dto.ResetPasswordDTO;
import com.example.movieticketingplatform.model.domain.dto.UserRegisterDTO;
import com.example.movieticketingplatform.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IUserService;
import com.example.movieticketingplatform.model.domain.User;

import javax.xml.transform.Result;
import java.util.Map;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;


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
    }//登录接口
    @PostMapping("login")
    public JsonResponse login(@RequestBody User user) throws Exception {
        User login = userService.login(user);
        return JsonResponse.success(login);
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
            // 验证当前用户是否登录
            User currentUser = SessionUtils.getCurrentUserInfo();
            if (currentUser == null) {
                return JsonResponse.failure("请先登录").setCode(401);
            }

            // 确保用户只能更新自己的信息，管理员除外
            if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(user.getId())) {
                return JsonResponse.failure("没有权限修改此用户信息").setCode(403);
            }

            // 参数校验
            if (user == null || user.getId() == null) {
                return JsonResponse.failure("参数错误：用户ID不能为空").setCode(400);
            }

            // 检查用户是否存在
            User existingUser = userService.getById(user.getId());
            if (existingUser == null) {
                return JsonResponse.failure("用户不存在").setCode(404);
            }

            // 校验合法电话号码
            if (user.getContactPhone() != null && !user.getContactPhone().equals(existingUser.getContactPhone())) {
                String phoneRegex = "^(1[3-9]\\d{9})$|^(0\\d{2,3}-?\\d{7,8})$";
                if (!user.getContactPhone().matches(phoneRegex)) {
                    return JsonResponse.failure("电话号码格式不正确").setCode(400);
                }
            }

            // 校验用户名
            if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
                if (userService.existsByUsername(user.getUsername())) {
                    return JsonResponse.failure("用户名已存在").setCode(409);
                }
            }

            // 校验邮箱
            if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
                if (userService.existsByEmail(user.getEmail())) {
                    return JsonResponse.failure("邮箱已被注册").setCode(409);
                }
            }

            // 执行更新
            boolean success = userService.updateUser(user);
            if (success) {
                // 更新session中的用户信息
                User updatedUser = userService.getById(user.getId());
                SessionUtils.saveCurrentUserInfo(updatedUser);
                return JsonResponse.successMessage("更新成功").setCode(0);
            } else {
                return JsonResponse.failure("更新失败").setCode(500);
            }
        } catch (Exception e) {
            return JsonResponse.failure("系统异常: " + e.getMessage()).setCode(500);
        }
    }



    /**
     * 发送注册验证码
     * 修正：统一返回类型为JsonResponse<Object>，避免泛型不匹配
     */
    @PostMapping("/send-code")
    public JsonResponse<Object> sendRegisterCode(@RequestParam String email) { // 改为Object泛型
        userService.sendRegisterCode(email);
        return JsonResponse.success(null).setMessage("验证码已发送至邮箱，5分钟内有效");
    }
    /**
     * 注册接口
     */
    @PostMapping("/register")
    public JsonResponse<Object> register(@RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return JsonResponse.success(null).setMessage("注册成功，请登录");
    }//重置验证码接口
    @PostMapping("/forgot/send-code")
    public JsonResponse<Object> sendResetCode(@RequestParam String email) {
        userService.sendResetPasswordCode(email);
        return JsonResponse.success(null)
                .setMessage("密码重置验证码已发送至邮箱，5分钟内有效");
    }//重置密码接口
    @PostMapping("/forgot/reset-password")
    public JsonResponse<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
        return JsonResponse.success(null)
                .setMessage("密码重置成功，请使用新密码登录");
    }

    @GetMapping("/getUsers")
    public JsonResponse getUsers() throws Exception{
        List<User> users = userService.getUsers();
        return JsonResponse.success(users);
    }
    /**
     * 登录用户修改密码
     */
    @PostMapping("/updatePassword")
    public JsonResponse<?> updatePassword(@RequestBody Map<String, String> params) {
        try {
            // 获取参数
            Long userId = Long.valueOf(params.get("userId"));
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");

            // 参数校验
            if (userId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
                return JsonResponse.failure("参数不完整").setCode(400);
            }

            // 验证用户存在性
            User user = userService.getById(userId);
            if (user == null) {
                return JsonResponse.failure("用户不存在").setCode(404);
            }

            // 验证原密码正确性
            if (!user.getPassword().equals(oldPassword)) { // 注意：实际项目中应使用加密验证
                return JsonResponse.failure("原密码不正确").setCode(400);
            }

            // 执行密码更新
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setPassword(newPassword); // 注意：实际项目中应加密存储
            boolean success = userService.updateById(updateUser);

            if (success) {
                return JsonResponse.successMessage("密码修改成功");
            } else {
                return JsonResponse.failure("修改失败").setCode(500);
            }
        } catch (NumberFormatException e) {
            return JsonResponse.failure("用户ID格式错误").setCode(400);
        } catch (Exception e) {
            return JsonResponse.failure("系统异常: " + e.getMessage()).setCode(500);
        }
    }
}