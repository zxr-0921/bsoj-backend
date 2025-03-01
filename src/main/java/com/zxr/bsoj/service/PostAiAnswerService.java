package com.zxr.bsoj.service;

import com.zxr.bsoj.model.entity.PostAiAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxr.bsoj.model.entity.Posts;

/**
* @author zxr0921
* @description 针对表【post_ai_answer(帖子AI回复)】的数据库操作Service
* @createDate 2025-02-21 13:09:04
*/
public interface PostAiAnswerService extends IService<PostAiAnswer> {

    // 调用AI接口回答
    int getPostsAiAnswer(Posts posts);
}
