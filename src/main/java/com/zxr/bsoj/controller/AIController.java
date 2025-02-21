package com.zxr.bsoj.controller;

import cn.hutool.json.JSONObject;
import com.zxr.bsoj.common.BaseResponse;
import com.zxr.bsoj.common.ResultUtils;
import com.zxr.bsoj.manager.AiManager;
import com.zxr.bsoj.model.dto.aiquestion.AIGenerateQuestionRequest;
import com.zxr.bsoj.model.dto.aiquestion.AIQuestionAnalysisRequest;
import com.zxr.bsoj.model.entity.Posts;
import com.zxr.bsoj.service.PostAiAnswerService;
import com.zxr.bsoj.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Arrays;

import static com.zxr.bsoj.constant.AIGenerateQuestionConstant.*;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AIController {

    @Resource
    private AiManager aiManager;

    @Resource
    private PostsService postsService;
    @Resource
    private PostAiAnswerService postAiAnswerService;
    /**
     * 思路分析
     *
     * @param question
     * @return
     */
    @PostMapping("/question/analysis")
    public BaseResponse<String> getAnswer(@RequestBody AIQuestionAnalysisRequest question) {
        String userMessage = question.getTitle() + "\n" + question.getContent();
        String s = aiManager.doSyncStableRequest(QUESTION_ANALYSIS_TIP, userMessage);
        return ResultUtils.success(s);
    }

    /**
     * 代码优化
     */
    @PostMapping("/code/optimize")
    public BaseResponse<String> codeOptimize(String code) {
        if (code == null || code.isEmpty()) {
            throw new RuntimeException("代码不能为空");
        }
        String result = aiManager.doSyncStableRequest(CODE_OPTIMIZE_TIP, code);
        return ResultUtils.success(result);
    }


    /**
     * 智能判题
     */
/*    @PostMapping("/judge")
    public BaseResponse<JSONObject> judge(@RequestBody String code, String input) {
        String result = aiManager.doSyncStableRequest("### 代码试运行服务,你是一个代码执行器 " +
                "你只会给出运行结果，不需要给出其他回答" +
                "请提供需要运行的代码及输入用例，系统将返回以下严格格式的试运行结果：" +
                "{   \"answer\": \"程序输出的字符串（执行失败则为null）\", " +
                "   \"message\": \"执行状态或错误原因（例如：语法错误/运行时异常）\" }" +
                "再次强调答案只返回json对象,并且自动帮我去掉markdown格式json表示，" +
                "如果程序不能执行，也只返回json对象，在message中给出信息" +
                "再次强调无论什么情况强制按照规定格式输出，禁止输出其他内容" +
                "不要解释，你只要执行代码并按照格式返回", code + "\n" + input);

        System.out.println("result: " + result);
//        // 正则匹配 JSON 内容（支持跨行）
        Pattern pattern = Pattern.compile(
                "```json\\s*(\\{.*?\\})\\s*```",
                Pattern.DOTALL);
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            String jsonStr = matcher.group(1).trim();
            // 提取并清理空白
            try {
                JSONObject json = new JSONObject(jsonStr);
                return ResultUtils.success(json);
            } catch (Exception e) {
                System.out.println("JSON 解析失败: " + e.getMessage());
                throw new RuntimeException("JSON 解析失败: " + e.getMessage());
            }
        } else {
            System.out.println("未找到 JSON 内容");
        }
        return ResultUtils.success(null);
    }*/

    /**
     * ai生成题目
     */
    @PostMapping("/generate/question")
    public BaseResponse<String> generateQuestion(@RequestBody AIGenerateQuestionRequest request) {
        String title = request.getTitle();
        String[] type = request.getType();
        String difficulty = request.getDifficulty();
        String userMessage = title + "\n" + Arrays.toString(type) + "\n" + difficulty;
        String result = aiManager.doSyncStableRequest(GENERATE_QUESTION_TIP, userMessage);
        return ResultUtils.success(result);
    }


    /**
     * ai回答帖子
     */
    @GetMapping("/answer/{id}")
    public BaseResponse<String> answer(@PathVariable("id") String id) {
        Posts posts = postsService.getById(id);
        int postsAiAnswer = postAiAnswerService.getPostsAiAnswer(posts);
        if (postsAiAnswer == 0) {
            throw new RuntimeException("回答失败");
        }
        return ResultUtils.success("回答成功");
    }
}



