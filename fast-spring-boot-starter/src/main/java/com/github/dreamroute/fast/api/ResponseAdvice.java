package com.github.dreamroute.fast.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.biz.exception.BizException;
import javax.biz.exception.InnerException;
import javax.validation.ValidationException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

/**
 * 1、处理返回值；
 * 2、处理异常，对于bizException是我们定义的异常，需要展示给前端用户，而对于其他异常，均是系统异常，需要告警处理
 *
 * @author w.dehai
 */
@Slf4j
@SuppressWarnings("ALL")
@RestControllerAdvice("cn.yzw.jc")
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
        log.warn("[业务异常]", e);
        return exception(e.getRespEnum());
    }

    @ExceptionHandler(InnerException.class)
    public Object innerException(InnerException e) {
        return bizException(new BizException(new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 700;
            }
            @Override
            public String getDesc() {
                return e.getDesc();
            }
        }, e));
    }

    /**
     * 未知异常，需要告警处理
     */
    @ExceptionHandler(Exception.class)
    public Object exception(Exception e) {
        log.error("[未知异常]：{}:{}", e.getClass(), e.getMessage(), e);
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

    /**
     * mybatis异常，mybatis插件抛出的异常会被封装成MyBatisSystemException，需要调用两次getCause()才能获取到我们自定义的异常
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public Object mybatisSystemException(Exception e) {
        Throwable cause = e.getCause().getCause();
        if (cause instanceof BizException) {
            log.error("[mybatisSystemException]：{}:{}", cause.getClass(), cause.getMessage(), cause);
            BizException bizException = (BizException) cause;
            return bizException(bizException);
        }

        return exception(e);
    }

    /**
     * 参数是List类型的json会报这种错
     */
    @ExceptionHandler(ValidationException.class)
    public Object constraintViolationException(ValidationException e) {
        log.warn("[参数校验异常]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 698;
            }
            @Override
            public String getDesc() {
                return e.getMessage();
            }
        };
        return exception(respEnumMarker);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validateException(MethodArgumentNotValidException e) {
        log.warn("[参数校验异常]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 698;
            }
            @Override
            public String getDesc() {
                List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
                return ofNullable(allErrors).orElseGet(ArrayList::new).stream().map(ObjectError::getDefaultMessage).distinct().collect(joining(", "));
            }
        };
        return exception(respEnumMarker);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("[类型转换错误, 详细错误]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 680;
            }
            @Override
            public String getDesc() {
                return e.getMessage();
            }
        };
        return exception(respEnumMarker);
    }

    @ExceptionHandler(RpcException.class)
    public Object rpcException(RpcException e) {
        log.error("[RPC调用异常]：{}:{}", e.getClass(), e.getMessage(), e);
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

