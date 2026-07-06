package com.huangxx.mis.service;

import com.huangxx.mis.common.SessionUser;
import com.huangxx.mis.repository.LoginRepository;
import com.huangxx.mis.repository.OperationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LoginService {

    private final LoginRepository loginRepository;
    private final OperationLogRepository operationLogRepository;

    public LoginService(LoginRepository loginRepository, OperationLogRepository operationLogRepository) {
        this.loginRepository = loginRepository;
        this.operationLogRepository = operationLogRepository;
    }

    @Transactional
    public Optional<SessionUser> login(String loginName, String password) {
        Optional<SessionUser> user = loginRepository.findActiveUser(loginName, password);
        if (user.isPresent()) {
            loginRepository.markLogin(user.get().userId());
            operationLogRepository.log(user.get(), "登录", "Huangxx_SystemUser11", "用户登录系统", "成功");
        }
        return user;
    }

    @Transactional
    public void changePassword(SessionUser user, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.isBlank() || newPassword.length() < 6) {
            throw new IllegalArgumentException("新密码至少 6 位");
        }
        if (!loginRepository.passwordMatches(user.userId(), oldPassword)) {
            throw new IllegalArgumentException("原密码不正确");
        }
        loginRepository.changePassword(user.userId(), newPassword);
        operationLogRepository.log(user, "修改密码", "Huangxx_SystemUser11", "用户修改本人密码", "成功");
    }
}
