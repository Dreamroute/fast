package javax.biz.exception;

import com.github.dreamroute.fast.api.RespEnumMarker;

/**
 * @author : w.dehai.2021.03.30
 */
public class SysException extends RuntimeException {

    private final RespEnumMarker respEnum;

    public SysException(RespEnumMarker re) {
        this.respEnum = re;
    }

    public SysException(RespEnumMarker re, Throwable e) {
        super(e);
        this.respEnum = re;
    }

    @Override
    public String toString() {
        return "错误码: " + respEnum.getCode() + ", 错误信息: " + respEnum.getDesc();
    }
}
