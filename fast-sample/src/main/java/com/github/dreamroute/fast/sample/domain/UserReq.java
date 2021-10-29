package com.github.dreamroute.fast.sample.domain;

import lombok.Data;

import java.util.Date;

/**
 * 描述：// TODO
 *
 * @author w.dehi.2021-10-29
 */
@Data
public class UserReq {
    private Long id;
    private String name;
    private Gender gender;
    private Date birthday;
}
