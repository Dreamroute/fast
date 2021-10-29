package com.github.dreamroute.fast.starter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：自定义http message转换器，主要用于处理枚举和日期等特殊类型
 *
 * @author w.dehi.2021-10-29
 */
public class CustomHttpMessageConverter extends FastJsonHttpMessageConverter {

    private static final String SUFFIX = "Str";

    @Override
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Field[] fields = ReflectUtil.getFields(o.getClass());
        Map<String, Object> data = new HashMap<>();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                Object v = ReflectUtil.getFieldValue(o, field);
                String fieldName = field.getName();
                data.put(fieldName, v);

                // 枚举
                if (v instanceof EnumMarker) {
                    data.put(fieldName, ((EnumMarker) v).getValue());
                    data.put(fieldName + SUFFIX, ((EnumMarker) v).getDesc());
                }

                // 日期
                if (v instanceof Date) {
                    data.put(fieldName + SUFFIX, DateUtil.format((Date) v, "yyyy-MM-dd HH:mm:ss.SSS"));
                }
            }
        }
        super.write(data, type, contentType, outputMessage);
    }
}
