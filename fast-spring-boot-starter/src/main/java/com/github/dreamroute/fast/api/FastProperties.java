package com.github.dreamroute.fast.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author w.dehai.2021/8/31.16:43
 */
@Data
@ConfigurationProperties(prefix = "fast")
public class FastProperties {
    private String path = "cn.yzw.jc";
}
