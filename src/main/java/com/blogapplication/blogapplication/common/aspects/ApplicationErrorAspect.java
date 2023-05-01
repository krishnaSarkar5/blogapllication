package com.blogapplication.blogapplication.common.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ApplicationErrorAspect {

    @AfterThrowing(pointcut = "execution( * com.blogapplication.blogapplication.user..*.*(..))" , throwing = "exception")
    public void printErrorLog(JoinPoint joinPoint, Exception exception){

        System.out.println(" executed method : "+joinPoint.getSignature().getName());
//        System.out.println(" executed method : "+request.getRequestURI().substring(request.getContextPath().length()));
        System.out.println(" exception occur in executed method : "+exception);
    }


}
