package com.github.dreamroute.fast.exception;

import com.github.dreamroute.fast.api.RespEnumMarker;
import lombok.Getter;

/**
 * 非业务异常，比较严重的，需要告警
 *
 * @author w.dehai
 */
@Getter
public class SysException extends BizException {

    public SysException(RespEnumMarker re) {
        super(re);
    }

    public SysException(RespEnumMarker re, Throwable e) {
        super(re, e);
    }

}
