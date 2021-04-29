package com.github.dreamroute.fast.sample.config;

import com.github.dreamroute.fast.api.ResponseAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author w.dehai.2021/4/29.17:02
 */
@Configuration
public class ResponseAdviceConfig {

    @Bean
    public ResponseAdvice responseAdvice() {
        return new ResponseAdvice();
    }

}
