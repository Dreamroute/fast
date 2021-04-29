package com.github.dreamroute.fast.sample.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.biz.exception.BizException;
import java.io.IOException;

import static com.github.dreamroute.fast.sample.config.RespEnum.BIZ;

/**
 * @author : w.dehai.2021.03.30
 */
@RestController
public class FastController {

    @GetMapping("/biz")
    public void biz() {
        throw new BizException(BIZ);
    }

    @GetMapping("/runtime")
    public void runtime() {
        throw new IllegalArgumentException("id不能为空");
    }

    @GetMapping("ex")
    public void ex() throws IOException {
        throw new IOException();
    }

    @GetMapping("thro")
    public void thro() throws Throwable {
        throw new Throwable();
    }

    @PostMapping("/castEx")
    public String castEx(@RequestBody Req req) {
        return "yzw";
    }

    @Data
    public static class Req {
        private Long id;
    }
}
