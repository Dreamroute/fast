package com.github.dreamroute.fast.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 被此注解标注的方法返回值不进行转换
 *
 * @author w.dehi
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface NonConvert {}
