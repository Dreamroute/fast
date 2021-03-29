package com.github.dreamroute.fast.exception;

import com.github.dreamroute.fast.api.RespEnumMarker;
import lombok.Getter;

/**
 * 业务异常，一般不需要告警
 *
 * @author w.dehai
 */
@Getter
public class BizException extends RuntimeException {

    private final RespEnumMarker respEnum;

    public BizException(RespEnumMarker re) {
        this.respEnum = re;
    }

    public BizException(RespEnumMarker re, Throwable e) {
        super(e);
        this.respEnum = re;
    }
}
