package com.zxr.bsoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxr.bsoj.manager.AiManager;
import com.zxr.bsoj.model.entity.PostAiAnswer;
import com.zxr.bsoj.model.entity.Posts;
import com.zxr.bsoj.service.PostAiAnswerService;
import com.zxr.bsoj.mapper.PostAiAnswerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zxr.bsoj.constant.AIGenerateQuestionConstant.REPLY_POST_TIP;

/**
* @author zxr0921
* @description 针对表【post_ai_answer(帖子AI回复)】的数据库操作Service实现
* @createDate 2025-02-21 13:09:04
*/
@Service
public class PostAiAnswerServiceImpl extends ServiceImpl<PostAiAnswerMapper, PostAiAnswer>
    implements PostAiAnswerService{

    @Resource
    private AiManager aiManager;

    @Resource
    private  PostAiAnswerMapper postAiAnswerMapper;
    @Override
    public int getPostsAiAnswer(Posts posts) {
        String title = posts.getTitle();
        String content = posts.getContent();
        String userMessage = new StringBuilder()
                .append(title).append("\n")
                .append(content).toString();
        // 调用AI接口回答
        String result = aiManager.doSyncStableRequest(REPLY_POST_TIP, userMessage);
        PostAiAnswer postAiAnswer = new PostAiAnswer();
        postAiAnswer.setAnswer(result);
        postAiAnswer.setPostsId(posts.getId());
        int insert = postAiAnswerMapper.insert(postAiAnswer);
        if (insert == 0) {
            throw new RuntimeException("插入失败");
        }
        return insert;
    }
}




