package com.zxr.bsoj.service;

import com.zxr.bsoj.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 帖子点赞服务测试
 */
@SpringBootTest
class PostThumbServiceTest {

    private static final User loginUser = new User();
    @Resource
    private PostThumbService postThumbService;

    @BeforeAll
    static void setUp() {
        loginUser.setId(1L);
    }

    @Test
    void doPostThumb() {
        int i = postThumbService.doPostThumb(1L, loginUser);
        Assertions.assertTrue(i >= 0);
    }
}
