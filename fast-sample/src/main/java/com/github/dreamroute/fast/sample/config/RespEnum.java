package com.github.dreamroute.fast.sample.config;

import com.github.dreamroute.fast.api.RespEnumMarker;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : w.dehai.2021.03.30
 */
@Getter
@AllArgsConstructor
public enum RespEnum implements RespEnumMarker {

    BIZ(601, "biz");

    private Integer code;
    private String desc;

}
