package com.github.dreamroute.fast.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.biz.exception.BizException;
import java.lang.reflect.Method;

/**
 * 1、处理返回值；
 * 2、处理异常，对于bizException是我们定义的异常，需要展示给前端用户，而对于其他异常，均是系统异常，需要告警处理
 *
 * @author w.dehai
 */
@Slf4j
@RestControllerAdvice("cn.yzw.jc")
@SuppressWarnings("ALL")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 被@NonConvert注解的方法不进行包装，直接返回结果
     */
    @Override
    public boolean supports(MethodParameter param, Class converterType) {
        Method method = param.getMethod();
        return method != null && !method.isAnnotationPresent(NonConvert.class);
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body instanceof RespEnumMarker ? RespUtil.exception((RespEnumMarker) body) : RespUtil.success(body);
    }

    /**
     * 业务异常，其他异常均需要告警处理
     */
    @ExceptionHandler(BizException.class)
    public Object bizException(BizException e) {
        log.error("[业务异常], " + e.toString(), e);
        return exception(e.getRespEnum());
    }

    /**
     * 未知异常，需要告警处理
     */
    @ExceptionHandler(Exception.class)
    public Object exception(Exception e) {
        log.error("[未知异常]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 699;
            }

            @Override
            public String getDesc() {
                return "o(╥﹏╥)o~~系统出异常啦!,请联系管理员!";
            }
        };
        return exception(respEnumMarker);
    }

    private Object exception(RespEnumMarker rem) {
        return RespUtil.exception(rem);
    }

}

