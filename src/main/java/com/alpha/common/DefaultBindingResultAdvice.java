package com.alpha.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * HibernateValidator错误结果处理切面
 */
public class DefaultBindingResultAdvice {
    @Pointcut()
    public void bindingResult() {
    }

    @Around("bindingResult()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.hasErrors()) {
                    FieldError fieldError = result.getFieldError();
                    if (fieldError != null) {
                        return ResultDTO.validateFailed(fieldError.getDefaultMessage());
                    } else {
                        return ResultDTO.validateFailed();
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
