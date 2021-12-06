package com.github.dreamroute.fast.api.serializer;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;

/**
 * 枚举序列化时候，将枚举类型转换成value类型
 *
 * @author w.dehai.2021/6/23.11:21
 */
public class EnumSerializer implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        if (value instanceof EnumMarker) {
            BeanGenerator generator = new BeanGenerator();
            generator.setSuperclass(object.getClass());
            generator.addProperty("desc", String.class);
            Object target = generator.create();
            BeanMap beanMap = BeanMap.create(target);
            beanMap.put("desc", "ms");
            System.err.println(target);
            object = target;
            try {
                Field[] fields = target.getClass().getFields();
                System.err.println(fields);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ((EnumMarker) value).getValue();
        }
        return value;
    }
}
