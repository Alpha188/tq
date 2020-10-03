package com.alpha.common;

import lombok.Data;

/**
 * 封装返回数据的类
 *
 * @author Alpha188
 * @date 2020/08/05
 */
@Data
public class ResultDTO {
    private Integer code;
    private String msg;
    private Object data;
    private Long count;

    public ResultDTO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultDTO(IResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public ResultDTO(IResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }

    public ResultDTO(IResultCode resultCode, Long count, Object data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
        this.count = count;
    }

    public ResultDTO(Integer code, String msg, Long count, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    public ResultDTO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResultDTO errorOf(IResultCode resultCode) {
        return new ResultDTO(resultCode, null);
    }

    public static ResultDTO errorOf() {
        return new ResultDTO(ResultCode.FAILED, null);
    }

    public static ResultDTO errorOf(Integer code, String msg, Object data) {
        return new ResultDTO(code, msg, data);
    }

    public static ResultDTO errorOf(Integer code, String msg) {
        return new ResultDTO(code, msg);
    }

    public static ResultDTO okOf(Integer code, String msg, Object data) {
        return new ResultDTO(code, msg, data);
    }

    public static ResultDTO okOf(Integer code, String msg, Long total, Object data) {
        return new ResultDTO(code, msg, total, data);
    }

    public static ResultDTO okOf(Long total, Object data) {
        return new ResultDTO(ResultCode.SUCCESS, total, data);
    }

    public static ResultDTO okOf() {
        return new ResultDTO(ResultCode.SUCCESS, null);
    }

    public static ResultDTO okOf(Object data) {
        return new ResultDTO(ResultCode.SUCCESS, data);
    }


    public static ResultDTO okOf(IResultCode resultCode, Long total, Object data) {
        return new ResultDTO(resultCode.getCode(), resultCode.getMsg(), total, data);
    }

    public static ResultDTO validateFailed(String errMsg) {
        return new ResultDTO(ResultCode.VALIDATE_FAILED.getCode(), errMsg, null);
    }

    public static ResultDTO validateFailed() {
        return new ResultDTO(ResultCode.VALIDATE_FAILED);
    }
}
