package com.github.dreamroute.fast.api;

import com.github.dreamroute.mybatis.pro.base.EnumMarker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.dreamroute.fast.api.JsonUtil.parseObj;
import static com.github.dreamroute.fast.api.JsonUtil.toJsonStr;
import static com.github.dreamroute.fast.api.JsonUtilTest.Gender.MALE;

/**
 * @author w.dehai.2021/6/25.15:55
 */
class JsonUtilTest {

    @Getter
    @AllArgsConstructor
    enum Gender implements EnumMarker {
        MALE(1, "男"),
        FEMALE(2, "女");

        private final Integer value;
        private final String desc;
    }

    @Data
    static class User {
        private Long id;
        private Gender gender;
    }

    @Test
    void toJsonStrTest() {
        User user = new User();
        user.setId(100L);
        user.setGender(MALE);
        String result = toJsonStr(user);
        String expect = "{\"gender\":1,\"id\":100}";
        Assertions.assertEquals(expect, result);
    }

    @Test
    void parseObjTest() {
        String input = "{\"gender\":1,\"id\":100}";
        User user = parseObj(input, User.class);
        User expect = new User();
        expect.setId(100L);
        expect.setGender(MALE);
        Assertions.assertEquals(expect, user);
    }

    @Test
    void parseArrTest() {
        // ignore.
    }
}
