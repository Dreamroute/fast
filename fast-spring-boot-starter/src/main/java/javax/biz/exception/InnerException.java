package javax.biz.exception;

import lombok.Getter;

/**
 * dubbo对于COMMON_EXCEPTION的desc反序列化取不到desc的值，所以这里定义一个内部异常来包装此类型的异常，
 * 在过滤器中判断是否是COMMON_EXCEPTION，如果是，那么就将异常设置成InnerException
 *
 * @author : w.dehai.2021.03.30
 */
@Getter
public class InnerException extends RuntimeException {

    private final String desc;

    public InnerException(String desc) {
        // 这里调用super，在responseadvice中打印时候message信息才不为null
        super(desc);
        this.desc = desc;
    }
}
