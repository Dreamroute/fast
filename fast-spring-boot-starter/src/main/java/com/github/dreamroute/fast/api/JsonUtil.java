package com.github.dreamroute.fast.api;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.dreamroute.fast.api.serializer.EnumParserConfig;
import com.github.dreamroute.fast.api.serializer.EnumSerializer;

import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * json工具类，用于对象的序列化和反序列化，工具类内部主要是解决枚举问题的序列化和反序列化问题。
 * 封装了FastJson的JSON.toJSONString、JSON.parseObject和JSON.parseArray方法
 *
 * @author w.dehai.2021/6/25.15:38
 */
public class JsonUtil {
    private JsonUtil() {}

    /**
     * 序列化对象：将对象转换成json字符串
     *
     * @param object pojo对象
     * @return 返回json字符串
     */
    public static String toJsonStr(Object object) {
        return toJSONString(object, new EnumSerializer());
    }

    /**
     * 序列化对象，可以自定义序列化方案
     *
     * @param object pojo对象
     * @param features FastJson的序列化特性参数
     */
    public static String toJsonStr(Object object, SerializerFeature... features) {
        return toJSONString(object, new EnumSerializer(), features);
    }

    /**
     * 反序列化对象：将字符串转换成pojo对象
     *
     * @param input json字符串
     * @param clazz pojo类型
     */
    public static <T> T parseObj(String input, Class<T> clazz) {
        return parseObject(input, clazz, new EnumParserConfig());
    }

    /**
     * 反序列化列表：将字符串转换成pojo列表
     *
     * @param input json字符串
     * @param clazz pojo类型
     */
    public static <T> List<T> parseArr(String input, Class<T> clazz) {
        return parseArray(input, clazz, new EnumParserConfig());
    }

}
