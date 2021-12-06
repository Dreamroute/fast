package com.github.dreamroute.fast.api.serializer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.github.dreamroute.fast.api.deserializer.EnumDeserializer;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;

import java.lang.reflect.Type;

public class EnumParserConfig extends ParserConfig {
    @Override
    public ObjectDeserializer getDeserializer(Class<?> cls, Type type) {

        if (EnumMarker.class.isAssignableFrom(cls)) {
            return new EnumDeserializer();
        }
        return super.getDeserializer(cls, type);
    }
}