package com.github.dreamroute.fast.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * 描述：针对jackson的枚举的反序列化
 *
 * @author w.dehi.2021-09-16
 */
public class EnumDeserializerForJackson extends JsonDeserializer<EnumMarker> {
    @Override
    public EnumMarker deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String name = p.currentName();
        Object value = p.getCurrentValue();
        Class<?> propertyType = BeanUtils.findPropertyType(name, value.getClass());
        if (EnumMarker.class.isAssignableFrom(propertyType)) {
            int intValue = p.getIntValue();
            Class c = propertyType;
            return (EnumMarker) EnumMarker.valueOf(c, intValue);
        }
        return null;
    }
}
