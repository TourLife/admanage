package com.example.admanage.apsect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;

/**
 * 注解，AOP统一处理异常信息
 */
@Aspect
@Component
public class HttpAspect {

    private final static Logger log = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.example.admanage.controller.UserController.*(..))")
    public void log(){

    }

    /**
     * 方法完成之前执行，记录访问的一些信息
     */
    @Before("log()")
    public void doBefor(JoinPoint joinPoint){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //url
        log.info("访问url = {}",request.getRequestURL());
        //method
        log.info("访问method_type = {}",request.getMethod());
        //ip
        log.info("访问ip = {}",request.getRemoteAddr());
        //classMethod
        log.info("访问class_method = {}",joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        //args
        log.info("访问args = {}",joinPoint.getArgs());
    }

    /**
     * 方法完成之后执行
     */
    @After("log()")
    public void doAfter(){
        log.info("方法执行完成={}","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * 方法完成之后执行，返回返回结果
     */
    @AfterReturning(returning = "object",pointcut = "log()")
    public void afterReturn(Object object){
        log.info("response={}",object);
    }

}
