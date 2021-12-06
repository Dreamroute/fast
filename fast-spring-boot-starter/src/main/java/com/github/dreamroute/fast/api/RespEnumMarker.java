package com.github.dreamroute.fast.api;

import java.io.Serializable;

/**
 * 返回值标记接口
 *
 * @author : w.dehai.2021.03.29
 */
public interface RespEnumMarker extends Serializable {

    /**
     * 返回值的code
     */
    Integer getCode();

    /**
     * 返回值的描述
     */
    String getDesc();

}
