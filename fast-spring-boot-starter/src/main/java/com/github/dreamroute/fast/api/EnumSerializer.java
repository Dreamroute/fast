package com.github.dreamroute.fast.api;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.dreamroute.mybatis.pro.core.typehandler.EnumMarker;

/**
 * 枚举序列化时候，将枚举类型转换成value类型
 *
 * @author w.dehai.2021/6/23.11:21
 */
public class EnumSerializer implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        if (value instanceof EnumMarker) {
            return ((EnumMarker) value).getValue();
        }
        return value;
    }
}
