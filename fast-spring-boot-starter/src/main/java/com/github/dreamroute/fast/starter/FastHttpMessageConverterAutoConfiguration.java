package com.github.dreamroute.fast.starter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.dreamroute.fast.api.DateSerializer;
import com.github.dreamroute.fast.api.EnumParserConfig;
import com.github.dreamroute.fast.api.EnumSerializer;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullBooleanAsFalse;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_ATOM_XML;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.APPLICATION_RSS_XML;
import static org.springframework.http.MediaType.APPLICATION_XHTML_XML;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.IMAGE_GIF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_MARKDOWN;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.http.MediaType.TEXT_XML;

/**
 * FastJSON转换
 *
 * @author w.dehai
 */
@Primary
@ConditionalOnClass(ConfigurableWebApplicationContext.class)
public class FastHttpMessageConverterAutoConfiguration implements WebMvcConfigurer {

    private static final String SUFFIX = "Str";

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        List<MediaType> mediaTypes = newArrayList(
                APPLICATION_JSON,
                APPLICATION_ATOM_XML,
                APPLICATION_FORM_URLENCODED,
                APPLICATION_OCTET_STREAM,
                APPLICATION_PDF,
                APPLICATION_RSS_XML,
                APPLICATION_XHTML_XML,
                APPLICATION_XML,
                IMAGE_GIF,
                IMAGE_JPEG,
                IMAGE_PNG,
                TEXT_EVENT_STREAM,
                TEXT_HTML,
                TEXT_MARKDOWN,
                TEXT_PLAIN,
                TEXT_XML,
                // .net端回调使用的是application/json-rpc，所以这里加上此content-type
                new MediaType("application", "json-rpc", StandardCharsets.UTF_8)
        );

        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter() {
            @Override
            public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                Field[] fields = ReflectUtil.getFields(o.getClass());
                Map<String, Object> data = new HashMap<>();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        Object v = ReflectUtil.getFieldValue(o, field);
                        String fieldName = field.getName();
                        data.put(fieldName, v);

                        if (v instanceof EnumMarker) {
                            data.put(fieldName, ((EnumMarker) v).getValue());
                            data.put(fieldName + SUFFIX, ((EnumMarker) v).getDesc());
                        }

                        if (v instanceof Date) {
                            data.put(fieldName + SUFFIX, DateUtil.format((Date) v, "yyyy-MM-dd HH:mm:ss.SSS"));
                        }
                    }
                }
                super.write(data, type, contentType, outputMessage);
            }
        };
        converter.setSupportedMediaTypes(mediaTypes);
        converter.setDefaultCharset(UTF_8);

        FastJsonConfig config = new FastJsonConfig();
        initSeria(config);
        initDeSeria(config);
        converter.setFastJsonConfig(config);

        // 移除默认
        converters.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
        // 将fast默认位置
        converters.add(0, converter);
    }

    /**
     * 序列化配置
     */
    private void initSeria(FastJsonConfig config) {
        config.setSerializerFeatures(
                // List字段如果为null, 输出为[], 而非null
                WriteNullListAsEmpty,
                // 字符类型字段如果为null, 输出为"", 而非null
                WriteNullStringAsEmpty,
                // Boolean字段如果为null, 输出为false, 而非null
                WriteNullBooleanAsFalse,
                // 消除对同一对象循环引用的问题, 默认为false
                DisableCircularReferenceDetect
        );

        // enum -> json, Date -> yyyy-MM-dd HH:mm:ss.SSS
//        config.setSerializeFilters(new EnumSerializer(), new DateSerializer());
    }

    /**
     * 反序列化配置
     */
    private void initDeSeria(FastJsonConfig config) {
        // json -> enum
        config.setParserConfig(new EnumParserConfig());
    }

}

