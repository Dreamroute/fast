package com.github.dreamroute.fast.starter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.dreamroute.fast.api.EnumParserConfig;
import com.github.dreamroute.mybatis.pro.core.typehandler.EnumMarker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
                TEXT_XML
        );

        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setSupportedMediaTypes(mediaTypes);
        converter.setDefaultCharset(UTF_8);

        // enum -> desc
        FastJsonConfig config = createConfig();
        converter.setFastJsonConfig(config);

        // value -> enum
        config.setParserConfig(new EnumParserConfig());

        // 移除默认
        converters.removeIf(conv -> conv instanceof MappingJackson2HttpMessageConverter);
        // 将fast默认位置
        converters.add(0, converter);
    }

    private FastJsonConfig createConfig() {
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                WriteNullListAsEmpty,                                           // List字段如果为null, 输出为[], 而非null
                WriteNullStringAsEmpty,                                       // 字符类型字段如果为null, 输出为"", 而非null
                WriteNullBooleanAsFalse,                                      // Boolean字段如果为null, 输出为false, 而非null
                DisableCircularReferenceDetect                           // 消除对同一对象循环引用的问题, 默认为false
        );

        // enum -> json
        ValueFilter vf = (object, name, value) -> {
            if (value instanceof EnumMarker) {
                return ((EnumMarker) value).getDesc();
            }
            return value;
        };
        config.setSerializeFilters(vf);
        return config;
    }
}

