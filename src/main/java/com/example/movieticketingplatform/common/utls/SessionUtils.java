package com.example.movieticketingplatform.common.utls;


import com.example.movieticketingplatform.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


public class SessionUtils {
    private static final String USERKEY = "sessionUser";

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    //用于管理员对用户的操作日志
    public static User getCurrentUserInfo() {
        return (User) session().getAttribute(USERKEY);
    }

    public static void saveCurrentUserInfo(User admin) {
        session().setAttribute(USERKEY, admin);
    }

}
