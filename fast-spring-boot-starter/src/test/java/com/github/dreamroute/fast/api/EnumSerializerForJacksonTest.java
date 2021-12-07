package com.github.dreamroute.fast.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.dreamroute.fast.api.serializer.EnumSerializerForJackson;
import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;

/**
 * 描述：// TODO
 *
 * @author w.dehi.2021-12-07
 */
class EnumSerializerForJacksonTest {
    @Getter
    @AllArgsConstructor
    public enum Gender implements EnumMarker {

        MAN(1, "男"),
        WOMAN(2, "女");
        private Integer value;

        private String desc;

    }

    @Data
    class User {
        private Long sysNo;
        private Gender gender;
    }

    @Test
    void test1() throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            User user = new User();
            user.setSysNo(1L);
            user.setGender(Gender.MAN);

            SimpleModule module = new SimpleModule();
            module.addSerializer(EnumMarker.class, new EnumSerializerForJackson());
            objectMapper.registerModule(module);

            String result = objectMapper.writeValueAsString(user);
            System.out.println("result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
