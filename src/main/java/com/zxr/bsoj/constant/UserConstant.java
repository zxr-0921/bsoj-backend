package com.zxr.bsoj.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 学生角色
     */
    String DEFAULT_ROLE = "student";
    /**
     * 教师角色
     */
    String TEACHER_ROLE = "teacher";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";
    String DEFAULT_PASSWORD = "12345678";
    String DEFAULT_USERNAME = "MOMO";

    // endregion
}
