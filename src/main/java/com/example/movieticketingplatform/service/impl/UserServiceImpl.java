package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.example.movieticketingplatform.model.domain.User;
import com.example.movieticketingplatform.mapper.UserMapper;
import com.example.movieticketingplatform.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieticketingplatform.service.OperationLogAUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OperationLogAUService operationLogAUService;

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

}
