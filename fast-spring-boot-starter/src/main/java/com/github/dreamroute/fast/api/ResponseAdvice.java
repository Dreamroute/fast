package com.github.dreamroute.fast.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
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

    @ExceptionHandler(InnerException.class)
    public Object innerException(InnerException e) {
        log.error("[业务异常], " + e.toString(), e);
        return bizException(new BizException(new RespEnumMarker() {
            public Integer getCode() {
                return 700;
            }
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

    /**
     * 参数是List类型的json会报这种错
     */
    @ExceptionHandler(ValidationException.class)
    public Object constraintViolationException(ValidationException e) {
        log.error("[参数校验异常]: ", e);
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
        log.error("[参数校验异常]: ", e);
        RespEnumMarker respEnumMarker = new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 698;
            }
            @Override
            public String getDesc() {
                MethodArgumentNotValidException ee = (MethodArgumentNotValidException) e;
                List<ObjectError> allErrors = ee.getBindingResult().getAllErrors();
                return ofNullable(allErrors).orElseGet(ArrayList::new).stream().map(ObjectError::getDefaultMessage).collect(joining(", "));
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
        log.error("[RPC调用异常]: ", e);
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

//    @ExceptionHandler(UncheckedExecutionException.class)
//    public Object unchecked(UncheckedExecutionException e) {
//        log.error("[Guava异常]: ", e);
//        if (e.getCause() instanceof BizException) {
//            return bizException((BizException) e.getCause());
//        }
//        throw e;
//    }

    private Object exception(RespEnumMarker rem) {
        return RespUtil.exception(rem);
    }

}

