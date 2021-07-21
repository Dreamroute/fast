package com.github.dreamroute.fast.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.alibaba.fastjson.JSON.toJSONString;
import static java.util.Optional.ofNullable;

/**
 * @author w.dehai.2021/7/21.16:16
 */
@Slf4j
@RestControllerAdvice
public class RequestAdvice extends RequestBodyAdviceAdapter {

    public RequestAdvice() {
        log.info("初始化RequestAdvice.");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        ofNullable(parameter).map(MethodParameter::getMethod).map(Method::getName).ifPresent(name -> log.info("\r\n请求接口: {}\r\n参数: {}", name, toJSONString(body, true)));
        return body;
    }
}
