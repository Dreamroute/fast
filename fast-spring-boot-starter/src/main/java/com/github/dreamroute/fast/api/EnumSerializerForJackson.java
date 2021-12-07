package com.github.dreamroute.fast.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;

import java.io.IOException;

/**
 * 枚举序列化时候，将枚举类型转换成value类型
 *
 * @author w.dehai.2021/6/23.11:21
 */
public class EnumSerializerForJackson extends JsonSerializer<EnumMarker> {

    @Override
    public void serialize(EnumMarker enumMarker, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(enumMarker.getValue());
    }
}
