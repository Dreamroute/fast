package com.github.dreamroute.fast.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                return "系统发生异常，请稍后再试。";
            }
        };
        return exception(respEnumMarker);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validateException(MethodArgumentNotValidException e) {
        log.error("[参数校验异常]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 698;
            }
            @Override
            public String getDesc() {
                FieldError fieldError = e.getBindingResult().getFieldError();
                return fieldError != null ? fieldError.getDefaultMessage() : e.getMessage();
            }
        };
        return exception(respEnumMarker);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object argumentException(IllegalArgumentException e) {
        log.error("[参数校验异常]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 697;
            }
            @Override
            public String getDesc() {
                return e.getMessage();
            }
        };
        return exception(respEnumMarker);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[类型转换错误, 详细错误]: ", e);
        return exception(e);
    }

    @ExceptionHandler(RpcException.class)
    public Object rpcException(RpcException e) {
        log.error("[RPC调用异常异常]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 695;
            }
            @Override
            public String getDesc() {
                return "服务发生异常，请稍后再试。";
            }
        };
        return exception(respEnumMarker);
    }

    private Object exception(RespEnumMarker rem) {
        return RespUtil.exception(rem);
    }

}

