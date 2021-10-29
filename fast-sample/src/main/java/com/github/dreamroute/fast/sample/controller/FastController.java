package com.github.dreamroute.fast.sample.controller;

import com.github.dreamroute.fast.sample.domain.UserReq;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.biz.exception.BizException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import static com.github.dreamroute.fast.sample.config.RespEnum.BIZ;

/**
 * @author : w.dehai.2021.03.30
 */
@Validated
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

    @PostMapping("/mm")
    public void mm(@Validated @RequestBody Mmm mmm) {
    }

    @PostMapping("/m4")
    public void m4(@Valid @RequestBody List<Mmm> mmm) {

    }

    @PostMapping("/enumTest")
    public UserReq enumTest(@Validated @RequestBody UserReq userReq) {
        System.err.println(userReq);
        return userReq;
    }

    @Data
    public static class Mmm {
        @NotNull
        private Long id;
        @NotEmpty(message = "用户名不能为空")
        private String name;
    }
}
