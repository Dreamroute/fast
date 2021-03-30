package javax.biz.exception;

import com.github.dreamroute.fast.api.RespEnumMarker;
import lombok.Getter;

/**
 * 业务异常，一般不需要告警
 * 包名说明：Dubbo的异常过滤器会将我们自定义异常进行包装，消费端无法获取到异常信息，在过滤器内部对于java和javax包下的异常不进行封装，所以这里使用javax开头
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

    @Override
    public String toString() {
        return "错误码: " + respEnum.getCode() + ", 错误信息: " + respEnum.getDesc();
    }
}
