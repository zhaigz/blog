package com.zgz.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: ControllerExceptionHandler
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/30 15:50
 * @Version: 1.0
 **/
/*全局异常处理
全局数据绑定,在每一个 Controller 的接口中，就都能够访问导致这些数据。
全局数据预处理*/
@ControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger((ControllerExceptionHandler.class));
    /*
     *
     * @param request
     * @Description:异常处理
     * @Return:
     */
    //标识这个类是可以做异常处理的 只要是Exception级别的都可以拦截
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL : {} ,Exception : {}",request.getRequestURL(),e);
        //判断如果有返回的状态码则不进行异常拦截,交给SpringBoot本身处理
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) !=null){
            throw e;
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("url",request.getRequestURL());
        mav.addObject("exception",e);
        mav.setViewName("error/error");
        return mav;
    }
}
