package com.zxr.bsoj.judge;

import com.zxr.bsoj.judge.codesandbox.model.JudgeInfo;
import com.zxr.bsoj.judge.strategy.*;
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
        // java的判题策略
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        // python
        if ("python".equals(language)){
            judgeStrategy= new PythonLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
