package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieticketingplatform.common.utls.VerifyCodeGenerator;
import com.example.movieticketingplatform.model.domain.User;
import com.example.movieticketingplatform.mapper.UserMapper;
import com.example.movieticketingplatform.model.domain.dto.ResetPasswordDTO;
import com.example.movieticketingplatform.model.domain.dto.UserRegisterDTO;
import com.example.movieticketingplatform.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieticketingplatform.service.OperationLogAUService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.movieticketingplatform.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OperationLogAUService operationLogAUService;

    @Autowired
    private MailService mailService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Page<User> pageList(User user, PageDTO pageDTO) {
        Page<User> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        page = userMapper.pageList(page,user);
        return page;
    }

    @Override
    public boolean existsByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        // 使用LambdaQueryWrapper构建查询条件
        return this.lambdaQuery()
                .eq(User::getUsername, username)
                .exists();  // 判断是否存在符合条件的记录
    }

    @Override
    public boolean existsByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        // 使用LambdaQueryWrapper构建查询条件
        return this.lambdaQuery()
                .eq(User::getEmail, email)
                .exists();  // 判断是否存在符合条件的记录
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {

        // 获取原始用户数据
        User originalUser = getById(user.getId());
        if (originalUser == null) {
            return false;
        }

        // 更新用户信息
        boolean isUpdated = updateById(user);

        if (isUpdated) {
            // 记录操作日志
            logUserChanges(originalUser, user);
        }

        return isUpdated;
    }

    /**
     * 记录用户信息变更日志
     */
    private void logUserChanges(User originalUser, User updatedUser) {
        Long currentUserId = 1L; // 临时设置为1，登录功能实现后替换

        // 检查用户名变更（仅当传入的 user 对象包含 username 时才记录）
        if (updatedUser.getUsername() != null && !originalUser.getUsername().equals(updatedUser.getUsername())) {
            operationLogAUService.logOperation(
                    currentUserId,
                    "修改用户名字",
                    "USER",
                    updatedUser.getId()
            );
        }

        // 检查邮箱变更（仅当传入的 user 对象包含 email 时才记录）
        if (updatedUser.getEmail() != null && !originalUser.getEmail().equals(updatedUser.getEmail())) {
            operationLogAUService.logOperation(
                    currentUserId,
                    "修改用户邮箱",
                    "USER",
                    updatedUser.getId()
            );
        }

        // 检查电话变更（仅当传入的 user 对象包含 contactPhone 时才记录）
        if (updatedUser.getContactPhone() != null && !originalUser.getContactPhone().equals(updatedUser.getContactPhone())) {
            operationLogAUService.logOperation(
                    currentUserId,
                    "修改用户电话",
                    "USER",
                    updatedUser.getId()
            );
        }

        // 检查角色变更（仅当传入的 user 对象包含 role 时才记录）
        if (updatedUser.getRole() != null && !originalUser.getRole().equals(updatedUser.getRole())) {
            operationLogAUService.logOperation(
                    currentUserId,
                    "修改用户角色",
                    "USER",
                    updatedUser.getId()
            );
        }

        // 检查状态变更（仅当传入的 user 对象包含 status 时才记录）
        if (updatedUser.getStatus() != null && !originalUser.getStatus().equals(updatedUser.getStatus())) {
            String operationType = updatedUser.getStatus().equals("SUSPENDED") ? "禁用用户" : "启用用户";
            operationLogAUService.logOperation(
                    currentUserId,
                    operationType,
                    "USER",
                    updatedUser.getId()
            );
        }
    }

    @Override
    @Transactional
    public boolean toggleUserStatus(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }

        String newStatus = user.getStatus().equals("SUSPENDED") ? "ACTIVE" : "SUSPENDED";
        boolean isUpdated = lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getStatus, newStatus)
                .update();

        if (isUpdated) {
            // 记录状态变更日志
            Long currentUserId = 1L; // 临时设置为1
            String operationType = newStatus.equals("SUSPENDED") ? "禁用用户" : "启用用户";
            operationLogAUService.logOperation(
                    currentUserId,
                    operationType,
                    "USER",
                    userId
            );
        }

        return isUpdated;
    }





    // 验证码有效期：5分钟
    private static final long CODE_EXPIRE_MINUTES = 5;
    // 新增：密码重置验证码的Redis键前缀（与注册区分）
    private static final String RESET_CODE_REDIS_KEY_PREFIX = "reset:verify:code:";
    // 定义Redis中验证码的键前缀（解决报错的核心）
    private static final String CODE_REDIS_KEY_PREFIX = "register:verify:code:";

    @Override
    public User login(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword());
        User one = userMapper.selectOne(queryWrapper);
        System.out.println("登录查询结果：" + one);
        return one;
    }
    @Override
    public void sendRegisterCode(String email) {
        //校验邮箱不为空
        if (!org.springframework.util.StringUtils.hasText(email)) {
            throw new IllegalArgumentException("邮箱不能为空");
        }

        //校验邮箱格式
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }

        //校验邮箱是否已注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email)
                .eq(User::getDeleted, false);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new IllegalArgumentException("该邮箱已被注册");
        }

        //生成6位验证码
        String verifyCode = VerifyCodeGenerator.generate6DigitCode();
        //log.info("生成验证码：{}，邮箱：{}", verifyCode, email);

        //发送验证码邮件
        try {
            mailService.sendVerifyCodeMail(email, verifyCode);
        } catch (Exception e) {
            //log.error("发送验证码邮件失败，邮箱：{}", email, e);
            throw new RuntimeException("验证码发送失败，请稍后重试");
        }

        //缓存验证码到Redis（设置5分钟过期）
        String redisKey = CODE_REDIS_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(redisKey, verifyCode, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        //log.info("验证码已缓存至Redis，键：{}，有效期：{}分钟", redisKey, CODE_EXPIRE_MINUTES);
    }
    @Override
    public void register(UserRegisterDTO registerDTO) {
        // 保存用户前输出数据
        //log.info("准备注册的用户信息：{}", registerDTO);
        String email = registerDTO.getEmail().trim();
        String phone = registerDTO.getPhone().trim();
        String password = registerDTO.getPassword().trim();
        String code = registerDTO.getCode().trim();
        String username = registerDTO.getUsername().trim();

        // 1. 基础参数校验
        if (!org.springframework.util.StringUtils.hasText(code)) {
            throw new IllegalArgumentException("请输入验证码");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于6位");
        }
        if (!org.springframework.util.StringUtils.hasText(username)) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("用户名长度必须在3-20之间");
        }
        // 校验用户名是否已存在
        checkUsernameExists(username);

        // 2. 校验邮箱是否已注册（复用发送验证码时的逻辑）
        checkEmailRegistered(email);

        // 3. 校验手机号是否已注册
        checkPhoneRegistered(phone);

        // 4. 校验验证码是否正确
        checkVerifyCode(email, code);

        // 5. 构建用户对象（直接存储明文密码）
        User user = new User();
        user.setEmail(email);
        user.setContactPhone(phone);
        user.setPassword(password); // 不加密，直接存储
        user.setUsername(username); // 存储用户名
        user.setRegistrationTime(LocalDateTime.now()); // 注册时间
        user.setStatus("ACTIVE"); // 默认激活状态
        user.setRole("USER"); // 默认普通用户
        user.setDeleted(false);

        // 6. 保存用户到数据库
        boolean saveSuccess = save(user);
        if (!saveSuccess) {
            throw new RuntimeException("注册失败，请稍后重试");
        }

        // 7. 注册成功后删除验证码（防止重复使用）
        redisTemplate.delete(CODE_REDIS_KEY_PREFIX + email);
        //log.info("用户注册成功，邮箱：{}，手机号：{}", email, phone);
    }
    @Override//发送重置验证码
    public void sendResetPasswordCode(String email) {
        // 1. 校验邮箱不为空
        if (!org.springframework.util.StringUtils.hasText(email)) {
            throw new IllegalArgumentException("邮箱不能为空");
        }

        // 2. 校验邮箱格式
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }

        // 3. 校验邮箱是否已注册（与注册相反：必须已注册）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email)
                .eq(User::getDeleted, false);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser == null) {
            throw new IllegalArgumentException("该邮箱未注册");
        }

        // 4. 生成6位验证码
        String verifyCode = VerifyCodeGenerator.generate6DigitCode();
        //log.info("生成密码重置验证码：{}，邮箱：{}", verifyCode, email);

        // 5. 发送验证码邮件（复用邮件服务，修改标题和内容）
        try {
            mailService.sendResetPasswordCodeMail(email, verifyCode);
        } catch (Exception e) {
            //log.error("发送密码重置验证码失败，邮箱：{}", email, e);
            throw new RuntimeException("验证码发送失败，请稍后重试");
        }

        // 6. 缓存验证码到Redis（单独的键前缀，避免与注册验证码冲突）
        String redisKey = RESET_CODE_REDIS_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(redisKey, verifyCode, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        //log.info("密码重置验证码已缓存，键：{}，有效期：{}分钟", redisKey, CODE_EXPIRE_MINUTES);
    }//重置密码
    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        String email = resetPasswordDTO.getEmail().trim();
        String code = resetPasswordDTO.getCode().trim();
        String newPassword = resetPasswordDTO.getNewPassword().trim();

        //基础参数校验
        if (!org.springframework.util.StringUtils.hasText(code)) {
            throw new IllegalArgumentException("请输入验证码");
        }
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            throw new IllegalArgumentException("密码长度必须在6-20之间");
        }

        //校验邮箱是否已注册（复用逻辑）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email)
                .eq(User::getDeleted, false);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new IllegalArgumentException("该邮箱未注册");
        }

        // 校验验证码是否正确（使用密码重置的Redis键）
        String redisKey = RESET_CODE_REDIS_KEY_PREFIX + email;
        String cachedCode = redisTemplate.opsForValue().get(redisKey);
        if (cachedCode == null) {
            throw new IllegalArgumentException("验证码已过期，请重新获取");
        }
        if (!cachedCode.equals(code)) {
            throw new IllegalArgumentException("验证码不正确");
        }

        //更新密码（不加密，与注册逻辑一致）
        user.setPassword(newPassword); // 直接存储新密码
        user.setLastLoginTime(null); // 重置登录时间（可选）
        boolean updateSuccess = updateById(user);
        if (!updateSuccess) {
            throw new RuntimeException("密码重置失败，请稍后重试");
        }
        //清除验证码（防止重复使用）
        redisTemplate.delete(redisKey);
        //log.info("用户密码重置成功，邮箱：{}", email);
    }

    // 辅助方法：校验手机号是否已注册
    private void checkPhoneRegistered(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getContactPhone, phone)
                .eq(User::getDeleted, false);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new IllegalArgumentException("该手机号已被注册");
        }
    }

    // 辅助方法：校验验证码
    private void checkVerifyCode(String email, String inputCode) {
        String redisKey = CODE_REDIS_KEY_PREFIX + email;
        String cachedCode = redisTemplate.opsForValue().get(redisKey);

        if (cachedCode == null) {
            throw new IllegalArgumentException("验证码已过期，请重新获取");
        }
        if (!cachedCode.equals(inputCode)) {
            throw new IllegalArgumentException("验证码不正确");
        }
    }

    // 新增：校验用户名是否已存在
    private void checkUsernameExists(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username)
                .eq(User::getDeleted, false);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new IllegalArgumentException("该用户名已被占用");
        }
    }

    // 辅助方法：校验邮箱是否已注册（提取复用）
    private void checkEmailRegistered(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email)
                .eq(User::getDeleted, false);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new IllegalArgumentException("该邮箱已被注册");
        }
    }
}