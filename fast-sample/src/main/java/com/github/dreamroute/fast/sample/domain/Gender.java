package com.github.dreamroute.fast.sample.domain;

import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述：// TODO
 *
 * @author w.dehi.2021-10-29
 */
@Getter
@AllArgsConstructor
public enum Gender implements EnumMarker {
    MALE(1, "男"), FEMALE(2, "女");

    private final Integer value;
    private final String desc;
}
