package javax.biz.exception;

import lombok.Getter;

/**
 * @author : w.dehai.2021.03.30
 */
@Getter
public class InnerException extends RuntimeException {

    private final String desc;

    public InnerException(String desc) {
        this.desc = desc;
    }
}
