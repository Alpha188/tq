package com.alpha.advice;

import com.alpha.common.DefaultBindingResultAdvice;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * HibernateValidator错误结果处理切面
 */
@Aspect
@Component
@Order(2)
public class BindingResultAdvice extends DefaultBindingResultAdvice {
    @Override
    @Pointcut("execution(public * com.alpha.controller.*.*(..))")
    public void bindingResult() {
    }

}
