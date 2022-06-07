package com.github.dreamroute.fast.api;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import org.springframework.util.StringUtils;

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
            // 这里不能使用intVal()或者integerVal()，因为有默认值0，当前端传入null时，会默认成0，造成业务错误
            String v = lexer.stringVal();
            if (StringUtils.isEmpty(v)) {
                return null;
            }
            return (T) EnumMarker.valueOf(cls, lexer.intValue());
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
