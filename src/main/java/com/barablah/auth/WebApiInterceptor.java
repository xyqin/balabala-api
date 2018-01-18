package com.barablah.auth;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API总定义头信息拦截器
 * Created by xyqin on 16/5/26.
 */
public class WebApiInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebApiContext context = WebApiContext.current();
        context.reset();
        context.setRequest(request);
        return true;
    }

}
