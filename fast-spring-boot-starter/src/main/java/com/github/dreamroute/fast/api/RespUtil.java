package com.github.dreamroute.fast.api;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 返回给前端的数据结构
 *
 * @author w.dehai
 */
@Data
public class RespUtil {

    @JSONField(ordinal = 1)
    private Integer code;
    @JSONField(ordinal = 2)
    private String desc;
    @JSONField(ordinal = 3)
    private Object data;

    private RespUtil(Object data, RespEnumMarker re) {
        this.data = data;
        this.code = re.getCode();
        this.desc = re.getDesc();
    }

    /**
     * 返回异常信息给前端，比如
     * <pre>
     *     {
     *         code: 601,
     *         desc: "用户名不能为空"
     *     }
     * </pre>
     */
    public static RespUtil exception(RespEnumMarker re) {
        return new RespUtil(null, re);
    }

    /**
     * 返回成功信息给前端
     * <pre>
     *     {
     *         code: 0,
     *         desc: "ok",
     *         data: {
     *             "id": 100,
     *             "name": "w.dehai"
     *         }
     *     }
     * </pre>
     */
    public static RespUtil success(Object data) {
        return new RespUtil(data, new RespEnumMarker() {
            @Override
            public Integer getCode() {
                return 0;
            }
            @Override
            public String getDesc() {
                return "ok";
            }
        });
    }

}
