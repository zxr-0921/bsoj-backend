package com.zxr.bsoj.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    API_REQUEST_ERROR(50002, "接口调用失败"),
    CREATE_ANNOUNCEMENT_ERROR(50003, "创建公告失败"),
    PUBLISH_ANNOUNCEMENT_ERROR(50004, "发布公告失败"),
    REVOKE_ANNOUNCEMENT_ERROR(50005, "撤销公告失败"),
    DELETE_ANNOUNCEMENT_ERROR(50006, "删除公告失败"),
    ANNOUNCEMENT_NOT_EXIST(50007, "公告不存在");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
