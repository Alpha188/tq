package com.alpha.common;

/**
 * 结果的状态码
 * 枚举了一些常用API操作码
 *
 * @author Alpha188
 * @date 2020/08/05
 */
public enum ResultCode implements IResultCode {
    /**
     * 成功
     */
    SUCCESS(0, "操作成功"),
    /**
     * 失败
     */
    FAILED(500, "操作失败"),
    /**
     * 验证失败
     */
    VALIDATE_FAILED(412, "参数检验失败"),
    /**
     * 未认证
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    /**
     * 拒绝访问
     */
    FORBIDDEN(403, "没有相关权限");
    private int code;
    private String msg;

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
