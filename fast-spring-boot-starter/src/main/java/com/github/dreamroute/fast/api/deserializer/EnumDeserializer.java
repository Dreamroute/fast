package com.github.dreamroute.fast.api.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;

import java.lang.reflect.Type;

/**
 * 请求枚举转换器，将请求中的value转换成枚举类型
 * 参考：https://blog.csdn.net/qq_26680031/article/details/83473643
 *
 * @author w.dehai
 */
@SuppressWarnings("unchecked")
public class EnumDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        Class<EnumMarker> cls = (Class<EnumMarker>) type;
        if (EnumMarker.class.isAssignableFrom(cls)) {
            return (T) EnumMarker.valueOf(cls, lexer.intValue());
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
