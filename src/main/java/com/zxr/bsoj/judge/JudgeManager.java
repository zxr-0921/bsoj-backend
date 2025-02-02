package com.zxr.bsoj.judge;

import com.zxr.bsoj.judge.codesandbox.model.JudgeInfo;
import com.zxr.bsoj.judge.strategy.DefaultJudgeStrategy;
import com.zxr.bsoj.judge.strategy.JavaLanguageJudgeStrategy;
import com.zxr.bsoj.judge.strategy.JudgeContext;
import com.zxr.bsoj.judge.strategy.JudgeStrategy;
import com.zxr.bsoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 * 不同语言有不同的判题策略
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
