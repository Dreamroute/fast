package com.github.dreamroute.fast.api.serializer;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.Date;

/**
 * 日期序列化，将日期转换成yyyy-MM-dd HH:mm:ss.SSS格式
 *
 * @author w.dehai
 */
public class DateSerializer implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        if (value instanceof Date) {
            return DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss.SSS");
        }
        return value;
    }
}
