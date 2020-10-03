package com.alpha.advice;


import com.alpha.common.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


/**
 * 通用异常处理
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    public final static Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultDTO badRequestException(Exception exception) {
        log.error("异常", exception);
        return ResultDTO.errorOf(HttpStatus.BAD_REQUEST.value(), "参数错误,请检测参数数据");
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultDTO uploadTooBig(Exception exception) {
        log.error("文件大小超出限制", exception);
        return ResultDTO.errorOf(HttpStatus.BAD_REQUEST.value(), "文件大小超出限制");
    }


    /**
     * 参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultDTO illegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常", e);
        return ResultDTO.errorOf(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 通用异常
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDTO exception(Throwable throwable) {
        log.error("系统异常", throwable);
        return ResultDTO.errorOf(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统内部错误");
    }
}

